package team.software.irbl.service.project;

import team.software.irbl.domain.Indicator;
import team.software.irbl.dto.project.ProjectInfo;
import team.software.irbl.util.Err;

import java.util.List;

public interface ProjectService {

    /**
     * 获取指定项目的评估指标
     * @param projectIndex
     * @return
     */
    public Indicator getIndicatorEvaluation(Integer projectIndex) throws Err;

    /**
     * 得到所有项目信息列表
     * @return
     * @throws Err
     */
    public List<ProjectInfo> getAllProjects() throws Err;
}
