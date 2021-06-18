package team.software.irbl.controller;

import org.junit.Test;
import team.software.irbl.service.file.CodeFileService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class CodeFileControllerTests {

    @Test
    public void readFileTest() throws Exception{
        CodeFileService codeFileService = mock(CodeFileService.class);

        CodeFileController controller = new CodeFileController(codeFileService);
        MockMvc mockMvc = standaloneSetup(controller).build();
        mockMvc.perform(get("/api/file/readFile/{fileIndex}",1))
                .andExpect(status().isOk());

        verify(codeFileService).readFile(1);
    }

    @Test
    public void localizationOfBugReportTest() throws Exception{
        CodeFileService codeFileService = mock(CodeFileService.class);

        CodeFileController controller = new CodeFileController(codeFileService);
        MockMvc mockMvc = standaloneSetup(controller).build();
        mockMvc.perform(get("/api/file/localizationOfBugReport/{reportIndex}",1))
                .andExpect(status().isOk());
        verify(codeFileService).getSortedFiles(1);

    }
}
