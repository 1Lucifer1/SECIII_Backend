package team.software.irbl.core;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import team.software.irbl.core.ProjectProcess;
import team.software.irbl.domain.Project;

import static org.junit.Assert.*;

@SpringBootTest
public class ProjectProcessTests {

    @Autowired
    private ProjectProcess process;

    @Test
    @Transactional
    @Rollback
    public void testTraverseAndSaveProject(){
        Project res = process.traverseAndSaveProject("swt-3.1");
        assertNotNull(res);
    }

    @Test
    @Transactional
    @Rollback
    public void testGetProjectFromDB(){
        Project res = process.getProjectFromDB(2);
        assertNotNull(res);
        assertEquals(res.getCodeFileCount(), res.getCodeFiles().size());
        assertNotEquals(0 ,res.getWordMap().size());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetProject(){
        Project res = process.getProject("swt-3.1");
        assertNotNull(res);
        assertEquals(res.getCodeFileCount(), res.getCodeFiles().size());
        assertNotEquals(0 ,res.getWordMap().size());
    }
}
