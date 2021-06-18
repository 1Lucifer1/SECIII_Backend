package team.software.irbl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = IRBLProdApplication.class)
@ActiveProfiles("prod")
class IRBLApplicationTests {

    @Test
    void contextLoads() {
    }

}
