package team.software.irbl.core;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    @Rollback
    public void testGetBugReportsFromXML(){
        List<BugReport> res = process.getBugReportsFromXML("Test.xml", 2);
        assertNotNull(res);
        assertNotEquals(0, res.size());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetBugReportsFromDB(){
        List<BugReport> res = process.getBugReportsFromDB( 2);
        assertNotNull(res);
        assertNotEquals(0, res.size());
    }

}
