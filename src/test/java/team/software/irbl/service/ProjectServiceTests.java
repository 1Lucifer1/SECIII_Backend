package team.software.irbl.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.software.irbl.dto.project.Indicator;
import team.software.irbl.service.project.ProjectService;
import team.software.irbl.util.Err;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectServiceTests {
    @Autowired
    private ProjectService projectService;

    @Test
    public void getIndicatorEvaluationTest() throws Err {
        Indicator indicator = projectService.getIndicatorEvaluation(2);
        assertThat(indicator.getTop1(), greaterThanOrEqualTo(0.071));
        assertThat(indicator.getTop5(), greaterThanOrEqualTo(0.316));
        assertThat(indicator.getTop10(), greaterThanOrEqualTo(0.49));
        assertThat(indicator.getMRR(), greaterThanOrEqualTo(0.20438985978353597));
        assertThat(indicator.getMAP(), greaterThanOrEqualTo(0.18109653077639296));
    }
}
