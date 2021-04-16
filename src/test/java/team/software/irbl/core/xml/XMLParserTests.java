package team.software.irbl.core.xml;

import org.junit.Before;
import org.junit.Test;
import team.software.irbl.core.store.XMLParser;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.FixedFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XMLParserTests {

    private List<BugReport> reports;

    @Before
    public void before(){
        List<FixedFile> fixedFiles = new ArrayList<>();
        fixedFiles.add(new FixedFile(-1, "org.eclipse.swt.widgets.Button.java"));
        fixedFiles.add(new FixedFile(-1, "org.eclipse.swt.widgets.ToolItem.java"));
        BugReport report = new BugReport(1, 78548, "2004-11-12 16:15:00", "2004-11-24 14:30:00",
                "[consistency] Button Selection fires before MouseUp",
                "- run the ControlExample, Button tab - turn on listeners MouseUp and Selection - click on an example Button -&gt; on OSX you'll get Selection - MouseUp -&gt; everywhere else you'll get MouseUp - Selection",
                fixedFiles);
        reports = new ArrayList<>();
        reports.add(report);
    }

    private void equal(BugReport report1, BugReport report2){
        assertEquals(report1.getProjectIndex(), report2.getProjectIndex());
        assertEquals(report1.getBugId(), report2.getBugId());
        assertEquals(report1.getOpenDate(), report2.getOpenDate());
        assertEquals(report1.getFixDate(), report2.getFixDate());
        assertEquals(report1.getSummary(), report2.getSummary());
        assertEquals(report1.getDescription(), report2.getDescription());
        assertEquals(report1.getFixedFiles().size(), report2.getFixedFiles().size());
        List<FixedFile> fixedFiles1 = report1.getFixedFiles();
        List<FixedFile> fixedFiles2 = report2.getFixedFiles();
        for(int i=0; i<fixedFiles1.size(); ++i){
            assertEquals(fixedFiles1.get(i).getFilePackageName(), fixedFiles2.get(i).getFilePackageName());
        }
    }

    @Test
    public void testGetBugReportsFromXML(){
        String filePath = "./IRBL/data/test/Test.xml";
        List<BugReport> reportList = XMLParser.getBugReportsFromXML(filePath, 1);
        assertNotNull(reportList);
        assertEquals(1, reportList.size());
        equal(reports.get(0), reportList.get(0));
    }
}
