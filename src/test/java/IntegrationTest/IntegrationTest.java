package IntegrationTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import team.software.irbl.controller.CodeFileController;
import team.software.irbl.controller.ProjectController;
import team.software.irbl.controller.ReportController;


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

//        CodeFileService codeFileService = mock(CodeFileService.class);
//        CodeFileController controller = new CodeFileController(codeFileService);
//        MockMvc mockMvc = standaloneSetup(controller).build();
//        mockMvc.perform(get("/api/file/readFile/{fileIndex}",1))
//                .andExpect(status().isOk());
////                .andExpect(view().name("readFile"));
//
//        verify(codeFileService).readFile(1);
    }
}
