package team.software.irbl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@MapperScan("team.software.irbl.mapper")
@SpringBootApplication
@Profile("prod")
public class IRBLProdApplication {
    public static void main(String[] args) {
        SpringApplication.run(IRBLProdApplication.class, args);
    }
}
