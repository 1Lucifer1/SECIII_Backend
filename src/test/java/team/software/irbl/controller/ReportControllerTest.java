package team.software.irbl.controller;

import org.junit.Test;
import team.software.irbl.dto.report.Report;
import team.software.irbl.service.report.ReportService;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


public class ReportControllerTest {

    @Test
    public void getAllReportsByProjectIndexTest() throws Exception{
        ReportService reportService = mock(ReportService.class);
        List<Report> reports = new ArrayList<>();
        when(reportService.getAllReportsByProjectIndex(1)).thenReturn(reports);

        ReportController controller = new ReportController(reportService);
        MockMvc mockMvc = standaloneSetup(controller).build();
        mockMvc.perform(get("api/report/getAllReportsByProjectIndex/{projectIndex}",1))
                .andExpect(status().isOk());
//                .andExpect(view().name(""));

        verify(reportService).getAllReportsByProjectIndex(1);
    }
}
