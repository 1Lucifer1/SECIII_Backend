package team.software.irbl.serviceImpl.report;

import org.springframework.stereotype.Service;
import team.software.irbl.dto.report.Report;
import team.software.irbl.service.report.ReportService;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Override
    public List<Report> getAllReportsByProjectIndex(Integer projectIndex) {
        if (projectIndex <= 0){
            return null;
        }
        return null;
    }
}
