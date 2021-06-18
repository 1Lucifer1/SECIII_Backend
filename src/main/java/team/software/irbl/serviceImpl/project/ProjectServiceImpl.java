package team.software.irbl.serviceImpl.project;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.software.irbl.core.IndicatorEvaluation;
import team.software.irbl.domain.Project;
import team.software.irbl.domain.Indicator;
import team.software.irbl.dto.project.ProjectInfo;
import team.software.irbl.mapper.*;
import team.software.irbl.service.project.ProjectService;
import team.software.irbl.util.Err;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private IndicatorEvaluation indicatorEvaluation;

    private RankRecordMapper rankRecordMapper;

    private BugReportMapper bugReportMapper;

    private FixedFileMapper fixedFileMapper;

    private ProjectMapper projectMapper;

    private IndicatorMapper indicatorMapper;

    @Autowired
    public ProjectServiceImpl(IndicatorEvaluation indicatorEvaluation, RankRecordMapper rankRecordMapper, BugReportMapper bugReportMapper, FixedFileMapper fixedFileMapper, ProjectMapper projectMapper, IndicatorMapper indicatorMapper) {
        this.indicatorEvaluation = indicatorEvaluation;
        this.rankRecordMapper = rankRecordMapper;
        this.bugReportMapper = bugReportMapper;
        this.fixedFileMapper = fixedFileMapper;
        this.projectMapper = projectMapper;
        this.indicatorMapper = indicatorMapper;
    }

    @Override
    public Indicator getIndicatorEvaluation(Integer projectIndex) throws Err {
        return indicatorMapper.selectOne(new QueryWrapper<Indicator>().eq("project_index", projectIndex));
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
