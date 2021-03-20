package team.software.irbl.IntegrationTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.software.irbl.controller.CodeFileController;
import team.software.irbl.controller.ProjectController;
import team.software.irbl.controller.ReportController;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    CodeFileController codeFileController;

    @Autowired
    ProjectController projectController;

    @Autowired
    ReportController reportController;

    @Test
    public void readFileTest() throws Exception{
//        codeFileController.readFile(1);
    }
}
