package team.software.irbl.core;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class VSMTests {
    @Autowired
    private VSM vsm;

    @Test
    @Transactional
    @Rollback
    public void testStartLocalRank(){
        vsm.startLocalRank();
    }
}
