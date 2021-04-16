package team.software.irbl.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.jdt.JavaParser;
import team.software.irbl.core.nlp.NLP;
import team.software.irbl.core.store.DBProcessor;
import team.software.irbl.core.store.DBProcessorFake;
import team.software.irbl.core.store.FileTranslator;
import team.software.irbl.core.vsm.VSM;
import team.software.irbl.core.store.XMLParser;
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
        Project project = new Project("swt-3.1");
        dbProcessor.saveProject(project);
        List<StructuredCodeFile> codeFiles = null;
        List<StructuredBugReport> bugReports = null;
        if(!hasPreprocess) {
            codeFiles = preProcessProject("swt-3.1", project.getProjectIndex());
            bugReports = preProcessBugReports("SWTBugRepository.xml", project.getProjectIndex());
            dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
            dbProcessor.saveBugReports(new ArrayList<>(bugReports));
            try {
                FileTranslator.writeBugReport(bugReports);
                FileTranslator.writeCodeFile(codeFiles);
                hasPreprocess = true;
            } catch (IOException e) {
                e.printStackTrace();
                Logger.errorLog("Saving json file failed.");
            }
        }else {
            try {
                bugReports = FileTranslator.readBugReport();
                codeFiles = FileTranslator.readCodeFile();
            } catch (IOException e) {
                e.printStackTrace();
                Logger.errorLog("Reading json file failed.");
            }
        }

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
            codeFiles = driver.preProcessProject("swt-3.1", 1);
            bugReports = driver.preProcessBugReports("SWTBugRepository.xml", 1);
            DBProcessorFake dbProcessor = new DBProcessorFake();
            dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
            dbProcessor.saveBugReports(new ArrayList<>(bugReports));
            try {
                FileTranslator.writeBugReport(bugReports);
                FileTranslator.writeCodeFile(codeFiles);
            } catch (IOException e) {
                e.printStackTrace();
                Logger.errorLog("Saving json file failed.");
            }
        }else {
            try {
                bugReports = FileTranslator.readBugReport();
                codeFiles = FileTranslator.readCodeFile();
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
        System.out.println(indicator.getTop1());
        System.out.println(indicator.getTop5());
        System.out.println(indicator.getTop10());
        System.out.println(indicator.getMRR());
        System.out.println(indicator.getMAP());
//        ranks.forEach(rankRecords -> {
//            rankRecords.forEach(rank -> {
//                System.out.println(rank.getFileRank() + ": " + rank.getCosineSimilarity());
//            });
//        });
    }
}
