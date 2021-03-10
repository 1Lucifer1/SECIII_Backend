package team.software.irbl.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import team.software.irbl.domain.CodeFile;
import team.software.irbl.mapper.CodeFileMapper;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DataBaseInsertTests {
    @Autowired
    private CodeFileMapper codeFileMapper;

    @Test
    public void testInsert(){
        System.out.println("----- insert method test -----");
        CodeFile codeFile = new CodeFile("test3.java", "test/test3.java", 0);
        int res = codeFileMapper.insert(codeFile);
        System.out.println(res);
    }

    @Test
    public void testInsertBatch(){
        System.out.println("----- insertBatchSomeColumn method test -----");
        List<CodeFile> codeFileList = new ArrayList<>();
        for(int i=0; i<10; ++i){
            codeFileList.add(new CodeFile("testn.java", "test/testn.java", 0));
        }
        int res = codeFileMapper.insertBatchSomeColumn(codeFileList);
        System.out.println(res);
    }

}
