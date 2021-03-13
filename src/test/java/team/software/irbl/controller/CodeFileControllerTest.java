package team.software.irbl.controller;

import org.junit.Test;
import team.software.irbl.service.file.CodeFileService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class CodeFileControllerTest {

    @Test
    public void readFileTest() throws Exception{
        CodeFileService codeFileService = mock(CodeFileService.class);
//        when(codeFileService.readFile(1)).thenReturn("文件内容");

        CodeFileController controller = new CodeFileController(codeFileService);
        MockMvc mockMvc = standaloneSetup(controller).build();
        mockMvc.perform(get("/api/file/readFile/{fileIndex}",1))
                .andExpect(status().isOk());
//                .andExpect(view().name("readFile"));

        verify(codeFileService).readFile(1);
    }
}
