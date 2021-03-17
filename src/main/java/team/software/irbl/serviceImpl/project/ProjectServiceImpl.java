package team.software.irbl.serviceImpl.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.software.irbl.core.IndicatorEvaluation;
import team.software.irbl.dto.project.Indicator;
import team.software.irbl.service.project.ProjectService;
import team.software.irbl.util.Err;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private IndicatorEvaluation indicatorEvaluation;

    @Override
    public Indicator getIndicatorEvaluation(Integer projectIndex) throws Err {
        Indicator indicator = new Indicator();
        indicator.setTop1(indicatorEvaluation.Top(projectIndex,1));
        indicator.setTop5(indicatorEvaluation.Top(projectIndex,5));
        indicator.setTop10(indicatorEvaluation.Top(projectIndex,10));
        indicator.setMRR(indicatorEvaluation.MRR(projectIndex));
        indicator.setMAP(indicatorEvaluation.MAP(projectIndex));
        return indicator;
    }
}
