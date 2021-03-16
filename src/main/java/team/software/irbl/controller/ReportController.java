package team.software.irbl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.software.irbl.service.report.ReportService;
import team.software.irbl.util.Err;
import team.software.irbl.util.Res;

/**
 * @Author: CGC
 * @Date:   2021-3-10
 */
@RestController
@RequestMapping("/api/report")
public class ReportController {
    private ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService){
        this.reportService = reportService;
    }

    @GetMapping("/getAllReportByProjectIndex/{projectIndex}")
    public Res getAllReportsByProjectIndex(@PathVariable Integer projectIndex) throws Err {
        return Res.success(reportService.getAllReportsByProjectIndex(projectIndex));
    }
}
