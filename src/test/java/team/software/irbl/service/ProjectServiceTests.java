package team.software.irbl.service;

import org.junit.Before;
import org.junit.Test;
import team.software.irbl.domain.Indicator;
import team.software.irbl.service.project.ProjectService;
import team.software.irbl.util.Err;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ProjectServiceTests {

    private ProjectService projectService;

    @Before
    public void before() throws Err {
        ProjectService projectService = mock(ProjectService.class);
        Indicator indicator = new Indicator();
        indicator.setProjectIndex(2);
        indicator.setTop1(0.5306);
        indicator.setTop5(0.7449);
        indicator.setTop10(0.8469);
        indicator.setMRR(0.6358);
        indicator.setMAP(0.5398);
        when(projectService.getIndicatorEvaluation(2)).thenReturn(indicator);

        this.projectService = projectService;
    }

    @Test
    public void getIndicatorEvaluationTest() throws Err {
        Indicator indicator = projectService.getIndicatorEvaluation(2);
        assertEquals(indicator.getProjectIndex(),(Integer) 2);
        assertThat(indicator.getTop1(), greaterThanOrEqualTo(0.398));
        assertThat(indicator.getTop5(), greaterThanOrEqualTo(0.673));
        assertThat(indicator.getTop10(), greaterThanOrEqualTo(0.826));
        assertThat(indicator.getMRR(), greaterThanOrEqualTo(0.53));
        assertThat(indicator.getMAP(), greaterThanOrEqualTo(0.45));
    }
}
