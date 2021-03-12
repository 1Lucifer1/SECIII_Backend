package team.software.irbl.database;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.domain.FileWord;
import team.software.irbl.domain.Project;
import team.software.irbl.domain.ProjectWord;
import team.software.irbl.mapper.CodeFileMapper;
import team.software.irbl.mapper.FileWordMapper;
import team.software.irbl.mapper.ProjectMapper;
import team.software.irbl.mapper.ProjectWordMapper;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DataBaseMethodsTests {
    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectWordMapper projectWordMapper;

    @Autowired
    private CodeFileMapper codeFileMapper;

    @Autowired
    private FileWordMapper fileWordMapper;

    @Test
    @Transactional
    @Rollback
    public void testInsertAndDelete(){
        System.out.println("----- insert method test -----");
        Project project = new Project("test");
        projectMapper.insert(project);
        CodeFile codeFile = new CodeFile("test1.java", "test/test3.java", project.getProjectIndex());
        codeFileMapper.insert(codeFile);
        ProjectWord projectWord = new ProjectWord("a",project.getProjectIndex());
        projectWordMapper.insert(projectWord);
        FileWord fileWord = new FileWord("a", codeFile.getFileIndex());
        fileWordMapper.insert(fileWord);
    }

    @Test
    @Transactional
    @Rollback
    public void testInsertBatch(){
        System.out.println("----- insertBatchSomeColumn method test -----");
        List<CodeFile> codeFileList = new ArrayList<>();
        for(int i=0; i<10; ++i){
            codeFileList.add(new CodeFile("testn.java", "test/testn.java", 1));
        }
        int res = codeFileMapper.insertBatchSomeColumn(codeFileList);
        assertEquals(10, res);
        for(CodeFile codeFile: codeFileList){
            System.out.println(codeFile.getFileIndex());
        }
    }

    @Test
    @Transactional
    @Rollback
    public void testInsertOrUpdateBatch(){
        System.out.println("----- insertOrUpdateBatchSomeColumn method test -----");
        List<CodeFile> codeFileList = codeFileMapper.selectList(new QueryWrapper<CodeFile>().eq("project_index", 1));
        //System.out.println(codeFileList);
        for(CodeFile codeFile: codeFileList){
            codeFile.setWordCount(50);
        }
        int res = codeFileMapper.insertOrUpdateBatch(codeFileList);
        System.out.println(res);
    }

}
