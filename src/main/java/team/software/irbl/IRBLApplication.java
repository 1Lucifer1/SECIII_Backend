package team.software.irbl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import team.software.irbl.core.Driver;
import team.software.irbl.core.vsm.VSM;

@MapperScan("team.software.irbl.mapper")
@SpringBootApplication
public class IRBLApplication implements CommandLineRunner {

    @Autowired
    private Driver driver;

    public static void main(String[] args) {
        SpringApplication.run(IRBLApplication.class, args);
    }

    @Override
    public void run(String... args){
        driver.setHasPreprocess(true);
        driver.startLocalRank();
    }
}
