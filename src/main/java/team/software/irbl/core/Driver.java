package team.software.irbl.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.enums.ComponentType;
import team.software.irbl.core.utils.jdt.JavaParser;
import team.software.irbl.core.common.maptool.CodeFileMap;
import team.software.irbl.core.common.maptool.FilePathMap;
import team.software.irbl.core.common.maptool.PackageMap;
import team.software.irbl.core.utils.nlp.NLP;
import team.software.irbl.core.common.dbstore.DBProcessor;
import team.software.irbl.core.common.dbstore.DBProcessorFake;
import team.software.irbl.core.utils.filestore.FileTranslator;
import team.software.irbl.core.component.reporterComponent.ReporterRank;
import team.software.irbl.core.component.similarReportComponent.SimilarReportRank;
import team.software.irbl.core.component.stacktraceComponent.StackRank;
import team.software.irbl.core.component.structureComponent.StructureRank;
import team.software.irbl.core.utils.filestore.XMLParser;
import team.software.irbl.core.component.versionHistoryComponent.VersionHistoryRank;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.domain.Project;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.dto.project.Indicator;
import team.software.irbl.util.Logger;
import team.software.irbl.util.SavePath;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Driver {

    private double[] weights = {1, 2, 0.5, 0.1, 0.1};
    private  DBProcessor dbProcessor;

    @Autowired
    public Driver(DBProcessor dbProcessor){
        this.dbProcessor = dbProcessor;
    }

    /**
     * 根据错误报告对指定项目源文件排序
     * @param projectName
     * @param forcePreprocess 指定是否强制重新预处理
     * @return 附带源文件相似度排序的BugReport列表
     */
    public List<BugReport> startRank(String projectName, boolean forcePreprocess){
        long startTime = System.currentTimeMillis();
        Project project = dbProcessor.getProjectByName(projectName);
        boolean isNewProject = false;
        if(project == null) {
            project = new Project(projectName);
            dbProcessor.saveProject(project);
            isNewProject = true;
        }
        List<StructuredBugReport> bugReports;
        List<StructuredCodeFile> codeFiles;
        // 如果项目要求重新预处理（spring boot运行时使用nlp处理会出错，故不推荐运行spring boot时再进行预处理）
        if(forcePreprocess){
            dbProcessor.cleanProject(project.getProjectIndex());
            // 预处理
            codeFiles = preProcessProject(projectName, project.getProjectIndex());
            bugReports = preProcessBugReports(projectName, project.getProjectIndex());
            if(bugReports == null || bugReports.size()==0 || codeFiles.size() == 0){
                Logger.errorLog("File preprocess failed.");
                return null;
            }
            // 数据库存保存读取的基础信息
            dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
            // aspectj的bug report中使用文件路径来对应代码文件
            CodeFileMap codeFileMap;
            if(project.getProjectName().equals("aspectj")) codeFileMap = new  FilePathMap(new ArrayList<>(codeFiles));
            else codeFileMap = new PackageMap(new ArrayList<>(codeFiles));
            dbProcessor.saveBugReports(new ArrayList<>(bugReports), codeFileMap);
            project.setCodeFileCount(codeFiles.size());
            project.setReportCount(bugReports.size());
            dbProcessor.updateProject(project);
            // 保存预处理结果
            FileTranslator.writeBugReport(bugReports,SavePath.getPreProcessReportPath(projectName));
            FileTranslator.writeCodeFile(codeFiles,SavePath.getPreProcessSourcePath(projectName));
        }else{
            // 不需要预处理则直接读取保存预处理内容的文件
            bugReports = FileTranslator.readBugReport(SavePath.getPreProcessReportPath(projectName));
            codeFiles = FileTranslator.readCodeFile(SavePath.getPreProcessSourcePath(projectName));
            if(bugReports==null || codeFiles == null){
                Logger.errorLog("Reading preprocessed files failed.");
                return null;
            }
            // 清空预处理数据中的可能的失效部分
            int projectIndex = project.getProjectIndex();
            bugReports.forEach(report -> {
                report.setProjectIndex(projectIndex);
                report.setReportIndex(-1);
            });
            codeFiles.forEach(codeFile -> {
                codeFile.setProjectIndex(projectIndex);
                codeFile.setFileIndex(-1);
            });
            if(isNewProject){
                // 数据库存保存读取的基础信息
                dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
                dbProcessor.saveBugReports(new ArrayList<>(bugReports), new FilePathMap(new ArrayList<>(codeFiles)));
                project.setCodeFileCount(codeFiles.size());
                project.setReportCount(bugReports.size());
                dbProcessor.updateProject(project);
            }else {
                List<CodeFile> codeFilesFromDB = dbProcessor.getCodeFilesByProjectIndex(projectIndex);
                List<BugReport> bugReportsFromDB = dbProcessor.getBugReportsByProjectIndex(projectIndex);
                bugReportsFromDB.sort(Comparator.comparingInt(BugReport::getBugId));
                if(bugReports.size() != bugReportsFromDB.size() || codeFiles.size() != codeFilesFromDB.size()){
                    Logger.errorLog("Bug report num or code file num changed.");
                    return null;
                }
                // 更新index
                for(int i=0; i<bugReports.size(); ++i){
                    bugReports.get(i).setReportIndex(bugReportsFromDB.get(i).getReportIndex());
                }
                FilePathMap filePathMap = new FilePathMap(codeFilesFromDB);
                codeFiles.forEach(codeFile -> {
                    codeFile.setFileIndex(filePathMap.getCodeFileFromMap(codeFile.getFilePath()).get(0).getFileIndex());
                });
            }
        }

        long preprocessEndTime = System.currentTimeMillis();
        Logger.log("Getting preprocessed files successes in " + (preprocessEndTime-startTime)/1000.0
                + " seconds and results in " + bugReports.size() + " bug reports and " + codeFiles.size() + " code files.");

        rank(codeFiles, bugReports, project);

        long endTime = System.currentTimeMillis();
        Logger.log("Rank for " + bugReports.size() + " bug reports among " + codeFiles.size() + " code files success in " +
                (endTime - preprocessEndTime)/1000.0 + " seconds and result.");
        return new ArrayList<>(bugReports);
    }

    private void rank(List<StructuredCodeFile> codeFiles, List<StructuredBugReport> bugReports, Project project){
        //int count = 0;
        PackageMap packageMap = new PackageMap(new ArrayList<>(codeFiles));
        StackRank stackRank = new StackRank(packageMap);
        StructureRank structureRank = new StructureRank(codeFiles);
        SimilarReportRank similarReportRank = new SimilarReportRank(new ArrayList<>(codeFiles));
        ReporterRank reporterRank = new ReporterRank(project.getProjectName(), packageMap);
        VersionHistoryRank versionHistoryRank = new VersionHistoryRank(new ArrayList<>(codeFiles), project);
        List<RankRecord> records = new ArrayList<>();
        for(StructuredBugReport bugReport: bugReports){
            ConcurrentHashMap<Integer, Double> scoreMap = new ConcurrentHashMap<>();
            List<RankRecord> recordList = stackRank.rank(bugReport);
            if(recordList != null) {
                //count++;
                recordList.forEach(rankRecord -> scoreMap.put(rankRecord.getFileIndex(), rankRecord.getScore()*weights[ComponentType.STACK.value()]));
            }
            if(recordList == null){
                recordList = structureRank.rank(bugReport);
                recordList.forEach(rankRecord -> scoreMap.put(rankRecord.getFileIndex(), rankRecord.getScore()*weights[ComponentType.STRUCTURE.value()]));
            }

            recordList = similarReportRank.rank(bugReport);
            recordList.forEach(rankRecord -> scoreMap.put(rankRecord.getFileIndex(), scoreMap.get(rankRecord.getFileIndex())+rankRecord.getScore()*weights[ComponentType.REPORT.value()]));

            recordList = reporterRank.rank(bugReport);
            recordList.forEach(rankRecord -> scoreMap.put(rankRecord.getFileIndex(), scoreMap.get(rankRecord.getFileIndex())+rankRecord.getScore()*weights[ComponentType.REPORTER.value()]));

            recordList = versionHistoryRank.rank(bugReport);
            recordList.forEach(rankRecord -> scoreMap.put(rankRecord.getFileIndex(), scoreMap.get(rankRecord.getFileIndex())+rankRecord.getScore()*weights[ComponentType.VERSION.value()]));

            recordList = new ArrayList<>();
            for(CodeFile codeFile: codeFiles){
                recordList.add(new RankRecord(bugReport.getReportIndex(), codeFile.getFileIndex(), -1, scoreMap.get(codeFile.getFileIndex())));
            }

            recordList.sort(Collections.reverseOrder());
            for(int i=0; i<recordList.size(); ++i){
                recordList.get(i).setFileRank(i+1);
            }
            bugReport.setRanks(recordList);
            records.addAll(recordList);
        }
        //Logger.log(count + " reports use stack rank.");
        // 保存排序结果
        dbProcessor.saveRankRecord(records);
    }

    private void setWeights(double[] weights){
        this.weights = weights;
    }

    private void saveCodeFilesABugReports(List<CodeFile> codeFiles, List<BugReport> bugReports, Project project){
        // 数据库存保存读取的基础信息
        dbProcessor.saveCodeFiles(codeFiles);
        // aspectj的bug report中使用文件路径来对应代码文件
        CodeFileMap codeFileMap;
        if(project.getProjectName().equals("aspectj")) codeFileMap = new  FilePathMap(new ArrayList<>(codeFiles));
        else codeFileMap = new PackageMap(codeFiles);
        dbProcessor.saveBugReports(bugReports, codeFileMap);
        project.setCodeFileCount(codeFiles.size());
        project.setReportCount(bugReports.size());
        dbProcessor.updateProject(project);
    }


    private List<StructuredCodeFile> preProcessProject(String projectName, int projectIndex){
        String dirPath = SavePath.getSourcePath(projectName) + "/";
        List<StructuredCodeFile> codeFiles = JavaParser.parseCodeFilesInDir(dirPath, projectIndex);

        Logger.log("found " + codeFiles.size() +   " code files.");
        // 对codeFile的结构化信息与原生文本分别进行nlp处理

        codeFiles.parallelStream().forEach(codeFile -> {
            codeFile.setTypes(NLP.standfordNLP(codeFile.getTypes(),true));
            codeFile.setComments(NLP.standfordNLP(codeFile.getComments(),true));
            codeFile.setFields(NLP.standfordNLP(codeFile.getFields(),true));
            codeFile.setMethods(NLP.standfordNLP(codeFile.getMethods(),true));
            // 限制单个文本长度
            List<String> contexts = new ArrayList<>();
            for(String context: split(codeFile.getContexts().get(0),5000)){
                contexts.addAll(NLP.standfordNLP(context, false));
            }
            codeFile.setContexts(contexts);
        });

        return codeFiles;
    }

    private List<String> split(String context, int length){
        List<String> res = new ArrayList<>();
        if(context.length() < length) {
            res.add(context);
        }else {
            int start = 0;
            while (start + length < context.length()){
                int last = start;
                start += length;
                while (start < context.length() && context.charAt(start)!=' ' && context.charAt(start)!='\n'){ start++;}
                res.add(context.substring(last, start));
            }
            if(start != context.length()) res.add(context.substring(start));
        }
        return res;
    }

    private List<StructuredBugReport> preProcessBugReports(String projectName, int projectIndex){
        String filePath = SavePath.getSourcePath(projectName) + "/bugRepository.xml";
        List<BugReport> rawReports = XMLParser.getBugReportsFromXML(filePath, projectIndex);
        if(rawReports == null){
            return null;
        }

        List<StructuredBugReport> reports = new ArrayList<>();

        for(BugReport rawReport : rawReports){
            StructuredBugReport report = new StructuredBugReport(rawReport);
            // 虽然没有格式化，但是由于专有名词出现频率不高，和格式化过的一起使用没有专有名词的停用词列表
            report.setDescriptionWords(NLP.standfordNLP(rawReport.getDescription(),true));
            report.setSummaryWords(NLP.standfordNLP(rawReport.getSummary(),true));
            reports.add(report);
        }
        return reports;
    }

    public static void evaluateAndSave(List<BugReport> bugReports, String savePath, String projectName){
        File saveResult = new File(SavePath.getSourcePath(savePath));
        IndicatorEvaluation indicatorEvaluation =new IndicatorEvaluation();
        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(bugReports);
        System.out.println("Top@1:  "+indicator.getTop1());
        System.out.println("Top@5:  "+indicator.getTop5());
        System.out.println("Top@10: "+indicator.getTop10());
        System.out.println("MRR:    "+indicator.getMRR());
        System.out.println("MAP:    "+indicator.getMAP());

        try{
            FileWriter writer = new FileWriter(saveResult, true);
            writer.write("Result evaluate for " + projectName +" :\n");
            writer.write("Top@1:  "+indicator.getTop1() + '\n');
            writer.write("Top@5:  "+indicator.getTop5() + '\n');
            writer.write("Top@10: "+indicator.getTop10() + '\n');
            writer.write("MRR:    "+indicator.getMRR() + '\n');
            writer.write("MAP:    "+indicator.getMAP() + '\n');
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String result = "result.txt";
        Driver driver = new Driver(new DBProcessorFake());

        driver.setWeights(new double[]{1, 2, 0.5, 0.1, 0.1});
        List<BugReport> bugReportsSwt = driver.startRank("swt-3.1", false);
        FileTranslator.writeOriginBugReport(bugReportsSwt, SavePath.getSourcePath("swt-res"));
        evaluateAndSave(bugReportsSwt, result, "swt-3.1");

        driver.setWeights(new double[]{1, 2, 0.1, 0.1, 0.1});
        List<BugReport> bugReportsEclipse = driver.startRank("eclipse-3.1", false);
        FileTranslator.writeOriginBugReport(bugReportsEclipse, SavePath.getSourcePath("eclipse-res"));
        evaluateAndSave(bugReportsEclipse, result, "eclipse-3.1");

        driver.setWeights(new double[]{1, 2, 0.5, 0.1, 0.1});
        List<BugReport> bugReportsAspectj = driver.startRank("aspectj", false);
        FileTranslator.writeOriginBugReport(bugReportsAspectj, SavePath.getSourcePath("aspectj-res"));
        evaluateAndSave(bugReportsAspectj, result, "aspectj");
        long processEndTime = System.currentTimeMillis();
        Logger.log("Finish all rank in " + (processEndTime-startTime)/1000.0 + " seconds");
    }
}
