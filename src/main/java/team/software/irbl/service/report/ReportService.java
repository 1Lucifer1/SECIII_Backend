package team.software.irbl.service.report;

import team.software.irbl.dto.report.Report;

import java.util.List;

public interface ReportService {
    public List<Report> getAllReportsByProjectIndex(Integer projectIndex);
}
