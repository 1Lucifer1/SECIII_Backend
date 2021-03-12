package team.software.irbl.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.software.irbl.dto.report.Report;
import team.software.irbl.service.report.ReportService;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportServiceTest {
    @Autowired
    private ReportService reportService;

    @Test
    public void getAllReportsByProjectIndexTest(){
        List<Report> reports = reportService.getAllReportsByProjectIndex(0);
        assertEquals(reports,null);
    }
}
