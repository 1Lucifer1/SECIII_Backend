package team.software.irbl.core;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import team.software.irbl.core.domain.StructuredBugReport;
import team.software.irbl.core.domain.StructuredCodeFile;
import team.software.irbl.core.vsm.VSM;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.FixedFile;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class VSMTests {

    @Test
    @Transactional
    @Rollback
    public void testRank(){
        VSM vsm = new VSM();
        java.util.List<StructuredBugReport> reportList = new ArrayList<>();
        List<String> test = new ArrayList<>();
        test.add("test");
        StructuredBugReport report = new StructuredBugReport(new BugReport(0,  0,  "1", "1", "test", "test", new ArrayList<>()));
        report.setDescriptionWords(test);
        report.setSummaryWords(test);
        reportList.add(report);
        List<StructuredCodeFile > codeFileList = new ArrayList<>();
        StructuredCodeFile sc = new StructuredCodeFile();
        sc.setComments(test);
        sc.setContexts(test);
        sc.setFields(test);
        sc.setMethods(test);
        sc.setTypes(test);
        codeFileList.add(sc);
        vsm.startRank(reportList, codeFileList);
        for (StructuredBugReport structuredBugReport : reportList) {
            structuredBugReport.getRanks().forEach(rankRecord->{
                System.out.println(rankRecord.getFileIndex() + ": " + rankRecord.getCosineSimilarity());
                Assert.assertEquals(rankRecord.getCosineSimilarity(), 1.0, 1);
            });
        }
    }
}
