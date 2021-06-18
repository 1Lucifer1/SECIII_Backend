package team.software.irbl.controller;

import org.junit.Test;
import team.software.irbl.service.project.ProjectService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ProjectControllerTests {

    @Test
    public void getIndicatorEvaluationTest() throws Exception {
        ProjectService projectService = mock(ProjectService.class);

        ProjectController controller = new ProjectController(projectService);

        MockMvc mockMvc = standaloneSetup(controller).build();
        mockMvc.perform(get("/api/project/getIndicatorEvaluation/{projectIndex}", 2))
                .andExpect(status().isOk());

        verify(projectService).getIndicatorEvaluation(2);
    }
}
