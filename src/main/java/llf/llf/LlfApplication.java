package llf.llf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("llf.llf.mapper")
public class LlfApplication {

    public static void main(String[] args) {
        SpringApplication.run(LlfApplication.class, args);
    }

}
