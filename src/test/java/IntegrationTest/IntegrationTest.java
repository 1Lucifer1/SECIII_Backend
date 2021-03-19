package IntegrationTest;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import team.software.irbl.controller.CodeFileController;
import team.software.irbl.service.file.CodeFileService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class IntegrationTest {
    @Test
    public void readFileTest() throws Exception{
        CodeFileService codeFileService = mock(CodeFileService.class);
        CodeFileController controller = new CodeFileController(codeFileService);
        MockMvc mockMvc = standaloneSetup(controller).build();
        mockMvc.perform(get("/api/file/readFile/{fileIndex}",1))
                .andExpect(status().isOk());
//                .andExpect(view().name("readFile"));

        verify(codeFileService).readFile(1);
    }
}
