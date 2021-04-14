package team.software.irbl.core;

import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.jdt.JavaParser;
import team.software.irbl.core.nlp.NLP;
import team.software.irbl.core.xml.XMLParser;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.util.Logger;
import team.software.irbl.util.SavePath;

import java.util.ArrayList;
import java.util.List;

public class Driver {

    public List<StructuredCodeFile> preProcessProject(String projectName, int projectIndex){
        String dirPath = SavePath.getAbsolutePath(projectName) + "/";
        List<StructuredCodeFile> codeFiles = JavaParser.parseCodeFilesInDir(dirPath, projectIndex);

        // 对codeFile的结构化信息与原生文本分别进行nlp处理
        for(StructuredCodeFile codeFile : codeFiles){
            codeFile.setTypes(NLP.standfordNLP(codeFile.getTypes()));
            codeFile.setComments(NLP.standfordNLP(codeFile.getComments()));
            codeFile.setFields(NLP.standfordNLP(codeFile.getFields()));
            codeFile.setMethods(NLP.standfordNLP(codeFile.getMethods()));
            codeFile.setContexts(NLP.standfordNLP(codeFile.getContexts()));
        }

        return codeFiles;
    }

    public List<StructuredBugReport> preProcessBugReports(String bugReportFile, int projectIndex){
        String filePath = SavePath.getAbsolutePath(bugReportFile);
        List<BugReport> rawReports =XMLParser.getBugReportsFromXML(filePath, projectIndex);
        if(rawReports == null){
            return null;
        }

        List<StructuredBugReport> reports = new ArrayList<>();

        for(BugReport rawReport : rawReports){
            StructuredBugReport report = new StructuredBugReport(rawReport);
            report.setDescriptionWords(NLP.standfordNLP(rawReport.getDescription()));
            report.setSummaryWords(NLP.standfordNLP(rawReport.getSummary()));
            reports.add(report);
        }
        return reports;
    }

    public static void main(String[] args) {
        Driver driver = new Driver();
        List<StructuredCodeFile> codeFiles = driver.preProcessProject("swt-3.1", 1);
        List<StructuredBugReport> bugReports = driver.preProcessBugReports("SWTBugRepository.xml", 1);
        VSM vsm = new VSM();
        List<List<RankRecord>> ranks  = vsm.startRank(bugReports, codeFiles);
        ranks.forEach(rankRecords -> {
            rankRecords.forEach(rank -> {
                System.out.println(rank.getFileRank() + ": " + rank.getCosineSimilarity());
            });
        });
    }
}
