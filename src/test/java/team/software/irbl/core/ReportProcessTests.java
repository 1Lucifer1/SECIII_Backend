package team.software.irbl.core;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team.software.irbl.domain.BugReport;
import team.software.irbl.core.ReportProcess;
import team.software.irbl.util.SavePath;

import java.util.List;
import static org.junit.Assert.*;

@SpringBootTest
public class ReportProcessTests {

    @Autowired
    private ReportProcess process;

    @Test
    public void testGetBugReportsFromXML(){
        List<BugReport> res = process.getBugReportsFromXML("SWTBugRepository.xml", 2);
        assertNotNull(res);
        assertNotEquals(0, res.size());
    }

    @Test
    public void testGetBugReportsFromDB(){
        List<BugReport> res = process.getBugReportsFromDB( 2);
        assertNotNull(res);
        assertNotEquals(0, res.size());
    }

}
