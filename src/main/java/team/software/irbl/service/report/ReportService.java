package team.software.irbl.service.report;

import team.software.irbl.dto.report.Report;
import team.software.irbl.util.Err;

import java.util.List;

public interface ReportService {
    /**
     * 读取指定项目下的所有缺陷报告列表
     * @param projectIndex
     * @return
     * @throws Err
     */
    public List<Report> getAllReportsByProjectIndex(Integer projectIndex) throws Err;
}
