package cn.atecut;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.atecut.dao")
public class AtEcutApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtEcutApplication.class, args);
    }

}
