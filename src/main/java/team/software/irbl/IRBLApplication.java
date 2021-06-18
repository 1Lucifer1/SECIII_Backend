package team.software.irbl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import team.software.irbl.core.Driver;

@MapperScan("team.software.irbl.mapper")
@SpringBootApplication
@Profile("dev")
public class IRBLApplication implements CommandLineRunner {

    @Autowired
    private Driver driver;

    public static void main(String[] args) {
        SpringApplication.run(IRBLApplication.class, args);
    }

    @Override
    public void run(String... args){
//        driver.startRank("swt-3.1", false);
//        driver.startRank("eclipse-3.1", false);
//        driver.startRank("aspectj", false);
    }
}
