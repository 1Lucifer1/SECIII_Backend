package team.software.irbl.core.common.vsm;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import team.software.irbl.core.Driver;
import team.software.irbl.core.IndicatorEvaluation;
import team.software.irbl.core.common.dbstore.DBProcessor;
import team.software.irbl.core.common.dbstore.DBProcessorFake;
import team.software.irbl.core.common.maptool.FilePathMap;
import team.software.irbl.core.component.similarReportComponent.SimilarReportRank;
import team.software.irbl.core.component.stacktraceComponent.StackRank;
import team.software.irbl.core.component.structureComponent.StructureRank;
import team.software.irbl.core.component.versionHistoryComponent.VersionHistoryRank;
import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.utils.filestore.FileTranslator;
import team.software.irbl.core.utils.filestore.XMLParser;
import team.software.irbl.core.utils.jdt.JavaParser;
import team.software.irbl.core.common.maptool.CodeFileMap;
import team.software.irbl.core.common.maptool.PackageMap;
import team.software.irbl.core.component.reporterComponent.ReporterRank;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.Project;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.dto.project.Indicator;
import team.software.irbl.util.Logger;
import team.software.irbl.util.SavePath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VSMTests {

    @Test
    public void testReporterComponent(){
        String projectName = "swt-3.1";

        List<BugReport> reports = XMLParser.getBugReportsFromXML(SavePath.getSourcePath(projectName) + "/bugRepository.xml", 1);
        List<StructuredCodeFile> codeFiles = JavaParser.parseCodeFilesInDir(SavePath.getSourcePath(projectName), 1);
        DBProcessor dbProcessor = new DBProcessorFake();
        dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
        CodeFileMap codeFileMap = new PackageMap(new ArrayList<>(codeFiles));
        dbProcessor.saveBugReports(reports, codeFileMap);
//        dbProcessor.saveBugReports(reports, new FilePathMap(new ArrayList<>(codeFiles)));

        ReporterRank reporterRank = new ReporterRank(projectName, codeFileMap);

        assert reports != null;
        reports.forEach(report -> {
            List<RankRecord> records = reporterRank.rank(report);
            records.sort(Collections.reverseOrder());
            for(int i=0; i<records.size(); ++i){
                records.get(i).setFileRank(i+1);
            }
            report.setRanks(records);
        });

        IndicatorEvaluation indicatorEvaluation = new IndicatorEvaluation();
        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(reports);
        indicator.print();
        Assert.assertTrue(indicator.getTop1() > 0.0);
        Assert.assertTrue(indicator.getTop5() > 0.0);
        Assert.assertTrue(indicator.getTop10() > 0.0);
        Assert.assertTrue(indicator.getMRR() > 0.0);
        Assert.assertTrue(indicator.getMAP() > 0.0);
    }

    @Test
    public void testSimilarReportComponent(){
        String projectName = "swt-3.1";
        List<StructuredBugReport> reports = FileTranslator.readBugReport(SavePath.getPreProcessReportPath(projectName));
        List<StructuredCodeFile> codeFiles = JavaParser.parseCodeFilesInDir(SavePath.getSourcePath(projectName)+'/', 1);
        DBProcessor dbProcessor = new DBProcessorFake();
        dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
        assert reports != null;
        dbProcessor.saveBugReports(new ArrayList<>(reports), new FilePathMap(new ArrayList<>(codeFiles)));

        SimilarReportRank similarReportRank = new SimilarReportRank(new ArrayList<>(codeFiles));

        reports.forEach(report -> {
            List<RankRecord> records = similarReportRank.rank(report);
            records.sort(Collections.reverseOrder());
            for(int i=0; i<records.size(); ++i){
                records.get(i).setFileRank(i+1);
            }
            report.setRanks(records);
        });

        IndicatorEvaluation indicatorEvaluation = new IndicatorEvaluation();
        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(new ArrayList<>(reports));
        indicator.print();
        Assert.assertTrue(indicator.getTop1() > 0.0);
        Assert.assertTrue(indicator.getTop5() > 0.0);
        Assert.assertTrue(indicator.getTop10() > 0.0);
        Assert.assertTrue(indicator.getMRR() > 0.0);
        Assert.assertTrue(indicator.getMAP() > 0.0);
    }

    @Test
    public void testStackTraceComponent(){
        String projectName = "swt-3.1";

        List<BugReport> reports = XMLParser.getBugReportsFromXML(SavePath.getSourcePath(projectName) + "/bugRepository.xml", 1);
        List<StructuredCodeFile> codeFiles = JavaParser.parseCodeFilesInDir(SavePath.getSourcePath(projectName+"/"), 1);
        DBProcessor dbProcessor = new DBProcessorFake();
        dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
//        dbProcessor.saveBugReports(reports, new FilePathMap(new ArrayList<>(codeFiles)));
        CodeFileMap codeFileMap = new PackageMap(new ArrayList<>(codeFiles));
        dbProcessor.saveBugReports(reports, codeFileMap);

        assert reports != null;

        List<BugReport> traceReports = Collections.synchronizedList(new ArrayList<>());
        StackRank stackRank = new StackRank(new PackageMap(new ArrayList<>(codeFiles)));
        reports.parallelStream().forEach(report -> {
            List<RankRecord> records = stackRank.rank(report);
            if (records != null) {
                for(int i=0; i<records.size(); i++){
                    RankRecord rankRecord = records.get(i);
                    rankRecord.setFileRank(i+1);
                }
                report.setRanks(records);
                traceReports.add(report);
            }
        });
        System.out.println(traceReports.size());

        IndicatorEvaluation indicatorEvaluation = new IndicatorEvaluation();
        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(traceReports);
        indicator.print();
        Assert.assertTrue(indicator.getTop1() > 0.0);
        Assert.assertTrue(indicator.getTop5() > 0.0);
        Assert.assertTrue(indicator.getTop10() > 0.0);
        Assert.assertTrue(indicator.getMRR() > 0.0);
        Assert.assertTrue(indicator.getMAP() > 0.0);
    }

    @Test
    public void testStructureComponent(){
        String projectName = "swt-3.1";
        List<StructuredBugReport> reports = FileTranslator.readBugReport(SavePath.getPreProcessReportPath(projectName));
        List<StructuredCodeFile> codeFiles = FileTranslator.readCodeFile(SavePath.getPreProcessSourcePath(projectName));

        assert reports != null;
        assert codeFiles != null;

        // 清空预处理数据中的可能的失效部分
        int projectIndex = 1;
        reports.forEach(report -> {
            report.setProjectIndex(projectIndex);
            report.setReportIndex(-1);
        });
        codeFiles.forEach(codeFile -> {
            codeFile.setProjectIndex(projectIndex);
            codeFile.setFileIndex(-1);
        });
        assert reports != null;
        DBProcessor dbProcessor = new DBProcessorFake();
        dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
        dbProcessor.saveBugReports(new ArrayList<>(reports), new FilePathMap(new ArrayList<>(codeFiles)));

        StructureRank structureRank = new StructureRank(new ArrayList<>(codeFiles));

        reports.forEach(report -> {
            List<RankRecord> records = structureRank.rank(report);
            records.sort(Collections.reverseOrder());
            for(int i=0; i<records.size(); ++i){
                records.get(i).setFileRank(i+1);
            }
            report.setRanks(records);
        });

        IndicatorEvaluation indicatorEvaluation = new IndicatorEvaluation();
        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(new ArrayList<>(reports));
        indicator.print();
        Assert.assertTrue(indicator.getTop1() > 0.0);
        Assert.assertTrue(indicator.getTop5() > 0.0);
        Assert.assertTrue(indicator.getTop10() > 0.0);
        Assert.assertTrue(indicator.getMRR() > 0.0);
        Assert.assertTrue(indicator.getMAP() > 0.0);
    }

    @Test
    public void testVersionHistoryComponent(){
        String projectName = "swt-3.1";
        int projectIndex = 1;
        Project project = new Project(projectName);
        project.setProjectIndex(projectIndex);

        List<BugReport> reports = XMLParser.getBugReportsFromXML(SavePath.getSourcePath(projectName) + "/bugRepository.xml", 1);
        List<StructuredCodeFile> codeFiles = JavaParser.parseCodeFilesInDir(SavePath.getSourcePath(projectName), 1);
        DBProcessor dbProcessor = new DBProcessorFake();
        dbProcessor.saveCodeFiles(new ArrayList<>(codeFiles));
        CodeFileMap codeFileMap = new PackageMap(new ArrayList<>(codeFiles));
        dbProcessor.saveBugReports(reports, codeFileMap);

        VersionHistoryRank versionHistoryRank = new VersionHistoryRank(new ArrayList<>(codeFiles), project);

        assert reports != null;
        reports.forEach(report -> {
            List<RankRecord> records = versionHistoryRank.rank(report);
            report.setRanks(records);
        });

        IndicatorEvaluation indicatorEvaluation = new IndicatorEvaluation();
        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(reports);
        indicator.print();

        Assert.assertTrue(indicator.getTop1() > 0.0);
        Assert.assertTrue(indicator.getTop5() > 0.0);
        Assert.assertTrue(indicator.getTop10() > 0.0);
        Assert.assertTrue(indicator.getMRR() > 0.0);
        Assert.assertTrue(indicator.getMAP() > 0.0);
    }

    @Test
    public void testDriver(){
        Driver driver = new Driver(new DBProcessorFake());
        List<BugReport> bugReportsSwt = driver.startRank("swt-3.1", true);

        IndicatorEvaluation indicatorEvaluation =new IndicatorEvaluation();
        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(bugReportsSwt);
        indicator.print();

        Assert.assertTrue(indicator.getTop1() > 0.0);
        Assert.assertTrue(indicator.getTop5() > 0.0);
        Assert.assertTrue(indicator.getTop10() > 0.0);
        Assert.assertTrue(indicator.getMRR() > 0.0);
        Assert.assertTrue(indicator.getMAP() > 0.0);
    }
}
