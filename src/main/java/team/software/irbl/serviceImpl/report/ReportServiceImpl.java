package team.software.irbl.serviceImpl.report;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.software.irbl.domain.BugReport;
import team.software.irbl.dto.report.Report;
import team.software.irbl.mapper.BugReportMapper;
import team.software.irbl.service.report.ReportService;
import team.software.irbl.util.Err;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private BugReportMapper bugReportMapper;

    private final static String PROJECT_NOTFOUND = "查找的项目不存在";

    @Autowired
    public ReportServiceImpl(BugReportMapper bugReportMapper){
        this.bugReportMapper = bugReportMapper;
    }

    @Override
    public List<Report> getAllReportsByProjectIndex(Integer projectIndex) throws Err {
        if (projectIndex <= 0){
            throw new Err(PROJECT_NOTFOUND);
        }
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("project_index", projectIndex);
        List<BugReport> bugReportList = bugReportMapper.selectByMap(conditions);
        List<Report> reports = new ArrayList<>();
        for(BugReport bugReport: bugReportList){
            Report report = new Report();
            BeanUtils.copyProperties(bugReport,report);
            reports.add(report);
        }
        return reports;
    }
}
