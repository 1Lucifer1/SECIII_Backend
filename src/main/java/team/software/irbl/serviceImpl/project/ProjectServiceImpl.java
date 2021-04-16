package team.software.irbl.serviceImpl.project;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.software.irbl.core.IndicatorEvaluation;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.FixedFile;
import team.software.irbl.dto.project.Indicator;
import team.software.irbl.mapper.BugReportMapper;
import team.software.irbl.mapper.FixedFileMapper;
import team.software.irbl.mapper.ProjectMapper;
import team.software.irbl.mapper.RankRecordMapper;
import team.software.irbl.service.project.ProjectService;
import team.software.irbl.util.Err;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private IndicatorEvaluation indicatorEvaluation;
    @Autowired
    private RankRecordMapper rankRecordMapper;
    @Autowired
    private BugReportMapper bugReportMapper;
    @Autowired
    private FixedFileMapper fixedFileMapper;

    @Override
    public Indicator getIndicatorEvaluation(Integer projectIndex) throws Err {
        Indicator indicator = new Indicator();

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("project_index", projectIndex);
        List<BugReport> bugReportList = bugReportMapper.selectByMap(conditions);

        for(BugReport bugReport: bugReportList){
            int reportIndex = bugReport.getReportIndex();
            bugReport.setFixedFiles(fixedFileMapper.selectList(new QueryWrapper<FixedFile>().eq("report_index", reportIndex)));

            Map<String, Object> conditions2 = new HashMap<>();
            conditions2.put("report_index", reportIndex);
            bugReport.setRanks(rankRecordMapper.selectByMap(conditions2));
        }

        int reportNum = bugReportList.size();

        indicator.setTop1(indicatorEvaluation.Top(1, reportNum, bugReportList));
        indicator.setTop5(indicatorEvaluation.Top(5, reportNum, bugReportList));
        indicator.setTop10(indicatorEvaluation.Top(10, reportNum, bugReportList));
        indicator.setMRR(indicatorEvaluation.MRR(reportNum, bugReportList));
        indicator.setMAP(indicatorEvaluation.MAP(reportNum, bugReportList));
        return indicator;
    }
}
