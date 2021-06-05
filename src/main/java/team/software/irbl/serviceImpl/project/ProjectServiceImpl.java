package team.software.irbl.serviceImpl.project;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.software.irbl.core.IndicatorEvaluation;
import team.software.irbl.domain.BugReport;
import team.software.irbl.domain.FixedFile;
import team.software.irbl.domain.Project;
import team.software.irbl.domain.RankRecord;
import team.software.irbl.dto.project.Indicator;
import team.software.irbl.dto.project.ProjectInfo;
import team.software.irbl.mapper.BugReportMapper;
import team.software.irbl.mapper.FixedFileMapper;
import team.software.irbl.mapper.ProjectMapper;
import team.software.irbl.mapper.RankRecordMapper;
import team.software.irbl.service.project.ProjectService;
import team.software.irbl.util.Err;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private IndicatorEvaluation indicatorEvaluation;

    private RankRecordMapper rankRecordMapper;

    private BugReportMapper bugReportMapper;

    private FixedFileMapper fixedFileMapper;

    private ProjectMapper projectMapper;

    @Autowired
    public ProjectServiceImpl(IndicatorEvaluation indicatorEvaluation, RankRecordMapper rankRecordMapper, BugReportMapper bugReportMapper, FixedFileMapper fixedFileMapper, ProjectMapper projectMapper) {
        this.indicatorEvaluation = indicatorEvaluation;
        this.rankRecordMapper = rankRecordMapper;
        this.bugReportMapper = bugReportMapper;
        this.fixedFileMapper = fixedFileMapper;
        this.projectMapper = projectMapper;
    }

    @Override
    public Indicator getIndicatorEvaluation(Integer projectIndex) throws Err {

        List<BugReport> bugReportList = bugReportMapper.selectList(new QueryWrapper<BugReport>().eq("project_index", projectIndex));

        for(BugReport bugReport: bugReportList){
            int reportIndex = bugReport.getReportIndex();
            bugReport.setFixedFiles(fixedFileMapper.selectList(new QueryWrapper<FixedFile>().eq("report_index", reportIndex)));

            List<RankRecord> rankRecordList = rankRecordMapper.selectList(new QueryWrapper<RankRecord>().eq("report_index", reportIndex));
            rankRecordList.sort(Comparator.comparing(RankRecord::getFileRank));
            bugReport.setRanks(rankRecordList);
        }

        Indicator indicator = indicatorEvaluation.getEvaluationIndicator(bugReportList);
        indicator.setProjectIndex(projectIndex);
        return indicator;
    }

    @Override
    public List<ProjectInfo> getAllProjects() throws Err {
        List<Project> projectList = projectMapper.selectList(null);
        List<ProjectInfo> projectInfoList = new ArrayList<>();
        for(Project project : projectList){
            ProjectInfo projectInfo = new ProjectInfo();
            BeanUtils.copyProperties(project, projectInfo);
            projectInfoList.add(projectInfo);
        }
        return projectInfoList;
    }
}
