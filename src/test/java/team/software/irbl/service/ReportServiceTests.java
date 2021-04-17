package team.software.irbl.service;

import org.junit.Before;
import org.junit.Test;
import team.software.irbl.dto.report.Report;
import team.software.irbl.mapper.BugReportMapper;
import team.software.irbl.service.report.ReportService;
import team.software.irbl.serviceImpl.report.ReportServiceImpl;
import team.software.irbl.util.Err;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ReportServiceTests {

    private ReportService reportService;

    @Before
    public void before() throws Err {
        ReportService reportService = mock(ReportService.class);
        List<Report> reportList = new ArrayList<>();
        Report report = new Report();
        report.setReportIndex(1);
        report.setBugId(1000);
        report.setOpenDate("2020-11-12 08:40:00");
        report.setFixDate("2020-12-12 08:40:00");
        report.setSummary("test bug report");
        reportList.add(report);
        when(reportService.getAllReportsByProjectIndex(1)).thenReturn(reportList);
        this.reportService = reportService;
    }

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
