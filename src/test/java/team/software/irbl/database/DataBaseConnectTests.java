package team.software.irbl.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.domain.FileWord;
import team.software.irbl.domain.Project;
import team.software.irbl.domain.ProjectWord;
import team.software.irbl.mapper.CodeFileMapper;
import team.software.irbl.mapper.FileWordMapper;
import team.software.irbl.mapper.ProjectMapper;
import team.software.irbl.mapper.ProjectWordMapper;

import java.util.List;

@SpringBootTest
public class DataBaseConnectTests {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectWordMapper projectWordMapper;

    @Autowired
    private CodeFileMapper codeFileMapper;

    @Autowired
    private FileWordMapper fileWordMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<Project> projectList = projectMapper.selectList(null);
        for(Project project:projectList) {
            System.out.println(project);
        }
        List<CodeFile> codeFileList = codeFileMapper.selectList(null);
        for(CodeFile codeFile:codeFileList){
            System.out.println(codeFile);
        }
        List<ProjectWord> projectWordList = projectWordMapper.selectList(null);
        for(ProjectWord word:projectWordList) {
            System.out.println(word);
        }
        List<FileWord> fileWordList = fileWordMapper.selectList(null);
        for(FileWord word:fileWordList) {
            System.out.println(word);
        }
    }
}
