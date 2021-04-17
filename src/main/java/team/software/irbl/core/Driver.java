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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Driver {

    private boolean hasPreprocess;

    private  DBProcessor dbProcessor;

    @Autowired
    public Driver(DBProcessor dbProcessor){
        this.dbProcessor = dbProcessor;
    }

    public void setHasPreprocess(boolean hasPreprocess) {
        this.hasPreprocess = hasPreprocess;
    }

    public void startLocalRank(){
        long startTime = System.currentTimeMillis();
        List<StructuredCodeFile> codeFiles = null;
        List<StructuredBugReport> bugReports = null;
        if(!hasPreprocess) {
            Project project = new Project("swt-3.1");
            dbProcessor.saveProject(project);
            codeFiles = preProcessProject("swt-3.1", project.getProjectIndex());
            bugReports = preProcessBugReports("SWTBugRepository.xml", project.getProjectIndex());
            if(bugReports==null || bugReports.size()==0 || codeFiles.size() == 0){
                Logger.errorLog("File preprocess failed.");
                return;
            }
            try {
                FileTranslator.writeBugReport(bugReports,"bugReportFile.json");
                FileTranslator.writeCodeFile(codeFiles,"codeFile.json");
                hasPreprocess = true;
            } catch (IOException e) {
                e.printStackTrace();
                Logger.errorLog("Saving json file failed.");
            }
            dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
            dbProcessor.saveBugReports(new ArrayList<>(bugReports));
            project.setCodeFileCount(codeFiles.size());
            project.setReportCount(bugReports.size());
            dbProcessor.updateProject(project);
        }else {
            try {
                bugReports = FileTranslator.readBugReport("bugReportFile.json");
                codeFiles = FileTranslator.readCodeFile("codeFile.json");
                if(bugReports==null || codeFiles == null || bugReports.size()==0 || codeFiles.size() == 0){
                    Logger.errorLog("Reading preprocessed files failed.");
                    return;
                }
                if(dbProcessor.getProjectByIndex(bugReports.get(0).getProjectIndex()) == null){
                    Project project = new Project("swt-3.1");
                    project.setProjectIndex(bugReports.get(0).getProjectIndex());
                    project.setReportCount(bugReports.size());
                    project.setCodeFileCount(codeFiles.size());
                    dbProcessor.saveProject(project);
                }
                dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
                dbProcessor.saveBugReports(new ArrayList<>(bugReports));
            } catch (IOException e) {
                e.printStackTrace();
                Logger.errorLog("Reading json file failed.");
            }
        }
        long preprocessEndTime = System.currentTimeMillis();
        Logger.log("Getting preprocessed files successes in " + (preprocessEndTime-startTime)/1000.0
                + " seconds and results in " + bugReports.size() + " bug reports and " + codeFiles.size() + " code files.");

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
        dbProcessor.saveRankRecord(records);
        long endTime = System.currentTimeMillis();
        Logger.log("Rank for " + bugReports.size() + " bug reports among " + codeFiles.size() + " code files success in " +
                (endTime - preprocessEndTime)/1000.0 + " seconds and result in " + records.size() + " rank records.");
    }

    private List<StructuredCodeFile> preProcessProject(String projectName, int projectIndex){
        String dirPath = SavePath.getAbsolutePath(projectName) + "/";
        List<StructuredCodeFile> codeFiles = JavaParser.parseCodeFilesInDir(dirPath, projectIndex);

        // 对codeFile的结构化信息与原生文本分别进行nlp处理
        for(StructuredCodeFile codeFile : codeFiles){
            codeFile.setTypes(NLP.standfordNLP(codeFile.getTypes(),true));
            codeFile.setComments(NLP.standfordNLP(codeFile.getComments(),true));
            codeFile.setFields(NLP.standfordNLP(codeFile.getFields(),true));
            codeFile.setMethods(NLP.standfordNLP(codeFile.getMethods(),true));
            codeFile.setContexts(NLP.standfordNLP(codeFile.getContexts(),false));
        }

        return codeFiles;
    }

    private List<StructuredBugReport> preProcessBugReports(String bugReportFile, int projectIndex){
        String filePath = SavePath.getAbsolutePath(bugReportFile);
        List<BugReport> rawReports =XMLParser.getBugReportsFromXML(filePath, projectIndex);
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
        boolean hasPreprocess = true;
        List<StructuredCodeFile> codeFiles = null;
        List<StructuredBugReport> bugReports = null;
        if(!hasPreprocess) {
            codeFiles = driver.preProcessProject("swt-3.1", 2);
            bugReports = driver.preProcessBugReports("SWTBugRepository.xml", 2);
            DBProcessorFake dbProcessor = new DBProcessorFake();
            dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
            dbProcessor.saveBugReports(new ArrayList<>(bugReports));
            try {
                FileTranslator.writeBugReport(bugReports,"bugReportFile.json");
                FileTranslator.writeCodeFile(codeFiles,"codeFile.json");
            } catch (IOException e) {
                e.printStackTrace();
                Logger.errorLog("Saving json file failed.");
            }
        }else {
            try {
                bugReports = FileTranslator.readBugReport("bugReportFile.json");
                codeFiles = FileTranslator.readCodeFile("codeFile.json");
            } catch (IOException e) {
                e.printStackTrace();
                Logger.errorLog("Reading json file failed.");
            }
        }

        VSM vsm = new VSM();
        vsm.startRank(bugReports, codeFiles);
        for(BugReport bugReport: bugReports){
            Logger.devLog("" + bugReport.getReportIndex());
            for(RankRecord record: bugReport.getRanks()){
                Logger.devLog("  " + record.getFileIndex() + " : " + record.getFileRank() + " , " +record.getCosineSimilarity());
            }
        }

        IndicatorEvaluation indicatorEvaluation =new IndicatorEvaluation();
        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(new ArrayList<>(bugReports));
        System.out.println("Top@1:  "+indicator.getTop1());
        System.out.println("Top@5:  "+indicator.getTop5());
        System.out.println("Top@10: "+indicator.getTop10());
        System.out.println("MRR:    "+indicator.getMRR());
        System.out.println("MAP:    "+indicator.getMAP());
//        ranks.forEach(rankRecords -> {
//            rankRecords.forEach(rank -> {
//                System.out.println(rank.getFileRank() + ": " + rank.getCosineSimilarity());
//            });
//        });
    }
}
