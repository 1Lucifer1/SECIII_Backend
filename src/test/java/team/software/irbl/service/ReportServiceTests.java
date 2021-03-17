package team.software.irbl.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.software.irbl.dto.report.Report;
import team.software.irbl.service.report.ReportService;
import team.software.irbl.util.Err;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportServiceTests {
    @Autowired
    private ReportService reportService;

    @Test
    public void getAllReportsByProjectIndexTest() throws Err {
        List<Report> reports = reportService.getAllReportsByProjectIndex(1);
        Report report = reports.get(0);

        assertEquals(1, report.getReportIndex());
        assertEquals(1000,report.getBugId());
        assertEquals("2020-11-12 08:40:00",report.getOpenDate());
        assertEquals("2020-12-12 08:40:00",report.getFixDate());
        assertEquals("test bug report",report.getSummary());
    }
}
