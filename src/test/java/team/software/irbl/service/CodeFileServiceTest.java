package team.software.irbl.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.software.irbl.service.file.CodeFileService;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeFileServiceTest {
    @Autowired
    private CodeFileService codeFileService;

    @Test
    public void readFileTest(){
        String fileContent = codeFileService.readFile(1);
        assertEquals(fileContent, "文件");
    }
}
