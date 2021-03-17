package team.software.irbl.service.project;

import team.software.irbl.dto.project.Indicator;
import team.software.irbl.util.Err;

public interface ProjectService {

    /**
     * 获取指定项目的评估指标
     * @param projectIndex
     * @return
     */
    public Indicator getIndicatorEvaluation(Integer projectIndex) throws Err;
}
