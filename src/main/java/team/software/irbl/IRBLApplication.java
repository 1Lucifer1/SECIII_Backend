package team.software.irbl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("team.software.irbl.mapper")
@SpringBootApplication
public class IRBLApplication {

    public static void main(String[] args) {
        SpringApplication.run(IRBLApplication.class, args);
    }

}
