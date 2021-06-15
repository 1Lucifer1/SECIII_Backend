package team.software.irbl.core.vsm;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import team.software.irbl.core.IndicatorEvaluation;
import team.software.irbl.core.common.dbstore.DBProcessor;
import team.software.irbl.core.common.dbstore.DBProcessorFake;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.utils.filestore.XMLParser;
import team.software.irbl.core.utils.jdt.JavaParser;
import team.software.irbl.core.common.maptool.CodeFileMap;
import team.software.irbl.core.common.maptool.FilePathMap;
import team.software.irbl.core.common.maptool.PackageMap;
import team.software.irbl.core.component.reporterComponent.ReporterRank;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.dto.project.Indicator;
import team.software.irbl.util.SavePath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VSMTests {

    @Test
    public void testRank(){
//        VSM vsm = new VSM();
//        java.util.List<StructuredBugReport> reportList = new ArrayList<>();
//        List<String> test = new ArrayList<>();
//        test.add("test");
//        StructuredBugReport report = new StructuredBugReport(new BugReport(0,  0,  "1", "1", "test", "test", new ArrayList<>()));
//        report.setDescriptionWords(test);
//        report.setSummaryWords(test);
//        reportList.add(report);
//        List<StructuredCodeFile > codeFileList = new ArrayList<>();
//        StructuredCodeFile sc = new StructuredCodeFile();
//        sc.setComments(test);
//        sc.setContexts(test);
//        sc.setFields(test);
//        sc.setMethods(test);
//        sc.setTypes(test);
//        codeFileList.add(sc);
//        vsm.startRank(reportList, codeFileList);
//        for (StructuredBugReport structuredBugReport : reportList) {
//            structuredBugReport.getRanks().forEach(rankRecord->{
//                System.out.println(rankRecord.getFileIndex() + ": " + rankRecord.getCosineSimilarity());
//                Assert.assertEquals(rankRecord.getCosineSimilarity(), 1.0, 1);
//            });
//        }
    }

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
}
