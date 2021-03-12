package team.software.irbl;

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
    public void testTraverseAndSaveProject(){
        Project project = process.traverseAndSaveProject();
        assertNotNull(project);
        System.out.println(project);
    }
}
