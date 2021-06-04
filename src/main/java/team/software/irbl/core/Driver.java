package team.software.irbl.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.jdt.JavaParser;
import team.software.irbl.core.nlp.NLP;
import team.software.irbl.core.dbstore.DBProcessor;
import team.software.irbl.core.dbstore.DBProcessorFake;
import team.software.irbl.core.filestore.FileTranslator;
import team.software.irbl.core.vsm.VSM;
import team.software.irbl.core.filestore.XMLParser;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.Project;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.dto.project.Indicator;
import team.software.irbl.util.Logger;
import team.software.irbl.util.SavePath;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Driver {

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
        List<StructuredCodeFile> codeFiles = null;
        List<StructuredBugReport> bugReports = null;
        Project project = dbProcessor.getProjectByName(projectName);
        // 如果项目未经过预处理（即不存在）或强制要求重新预处理
        // 注：使用DBProcessorFake时，project永不为null，故forcePreprocess即实际指定是否进行预处理
        if(forcePreprocess || project == null){
            // 如果project不存在则创建新project，否则将原project相关记录清空
            if(project == null) {
                project = new Project(projectName);
                dbProcessor.saveProject(project);
            }else{
                dbProcessor.cleanProject(project.getProjectIndex());
            }
            // 预处理
            codeFiles = preProcessProject(projectName, project.getProjectIndex());
            bugReports = preProcessBugReports(projectName, project.getProjectIndex());
            if(bugReports == null || bugReports.size()==0 || codeFiles.size() == 0){
                Logger.errorLog("File preprocess failed.");
                return null;
            }
            // 数据库存保存读取的基础信息
            dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
            //dbProcessor.saveBugReports(new ArrayList<>(bugReports));
            project.setCodeFileCount(codeFiles.size());
            project.setReportCount(bugReports.size());
            dbProcessor.updateProject(project);

            try {
                // 文件形式保存预处理结果
                FileTranslator.writeBugReport(bugReports,SavePath.getPreProcessReportPath(projectName));
                FileTranslator.writeCodeFile(codeFiles,SavePath.getPreProcessSourcePath(projectName));
            } catch (IOException e) {
                e.printStackTrace();
                Logger.errorLog("Saving json file failed.");
                dbProcessor.cleanProject(project.getProjectIndex());
                return null;
            }
        }else{
            // 不需要预处理则直接读取保存预处理内容的文件
            try {
                bugReports = FileTranslator.readBugReport(SavePath.getPreProcessReportPath(projectName));
                codeFiles = FileTranslator.readCodeFile(SavePath.getPreProcessSourcePath(projectName));
                if(bugReports==null || codeFiles == null || bugReports.size()==0 || codeFiles.size() == 0){
                    Logger.errorLog("Reading preprocessed files failed.");
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Logger.errorLog("Reading json file failed.");
                return null;
            }
        }

        long preprocessEndTime = System.currentTimeMillis();
        Logger.log("Getting preprocessed files successes in " + (preprocessEndTime-startTime)/1000.0
                + " seconds and results in " + bugReports.size() + " bug reports and " + codeFiles.size() + " code files.");

        // 使用vsm进行相似度排序
        VSM vsm = new VSM();
        vsm.startRank(bugReports, codeFiles);
        List<RankRecord> records = new ArrayList<>();
        for(BugReport bugReport: bugReports){
            Logger.devLog("" + bugReport.getReportIndex());
            for(RankRecord record: bugReport.getRanks()){
                records.add(record);
                Logger.devLog("  " + record.getFileIndex() + " : " + record.getFileRank() + " , " +record.getCosineSimilarity());
            }
        }
        // 保存排序结果
        dbProcessor.saveRankRecord(records);

        long endTime = System.currentTimeMillis();
        Logger.log("Rank for " + bugReports.size() + " bug reports among " + codeFiles.size() + " code files success in " +
                (endTime - preprocessEndTime)/1000.0 + " seconds and result in " + records.size() + " rank records.");
        return new ArrayList<BugReport>(bugReports);
    }

    private List<StructuredCodeFile> preProcessProject(String projectName, int projectIndex){
        String dirPath = SavePath.getSourcePath(projectName) + "/";
        List<StructuredCodeFile> codeFiles = JavaParser.parseCodeFilesInDir(dirPath, projectIndex);

        Logger.log("found " + codeFiles.size() + " code files.");
        // 对codeFile的结构化信息与原生文本分别进行nlp处理

        codeFiles.parallelStream().forEach(codeFile -> {
            codeFile.setTypes(NLP.standfordNLP(codeFile.getTypes(),true));
            codeFile.setComments(NLP.standfordNLP(codeFile.getComments(),true));
            codeFile.setFields(NLP.standfordNLP(codeFile.getFields(),true));
            codeFile.setMethods(NLP.standfordNLP(codeFile.getMethods(),true));
            codeFile.setContexts(NLP.standfordNLP(codeFile.getContexts(),false));
        });

        /*
        for(StructuredCodeFile codeFile : codeFiles){
            codeFile.setTypes(NLP.standfordNLP(codeFile.getTypes(),true));
            codeFile.setComments(NLP.standfordNLP(codeFile.getComments(),true));
            codeFile.setFields(NLP.standfordNLP(codeFile.getFields(),true));
            codeFile.setMethods(NLP.standfordNLP(codeFile.getMethods(),true));
            codeFile.setContexts(NLP.standfordNLP(codeFile.getContexts(),false));
        }
        */
        return codeFiles;
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

    public static void main(String[] args) {
        Driver driver = new Driver(new DBProcessorFake());
        List<BugReport> bugReports = driver.startRank("eclipse-3.1", true);

        File saveResult = new File(SavePath.getSourcePath("result1.txt"));
        IndicatorEvaluation indicatorEvaluation =new IndicatorEvaluation();
        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(bugReports);
        System.out.println("Top@1:  "+indicator.getTop1());
        System.out.println("Top@5:  "+indicator.getTop5());
        System.out.println("Top@10: "+indicator.getTop10());
        System.out.println("MRR:    "+indicator.getMRR());
        System.out.println("MAP:    "+indicator.getMAP());

        try{
            FileWriter writer = new FileWriter(saveResult);
            writer.write("Result evaluate for eclipse:\n");
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
}
