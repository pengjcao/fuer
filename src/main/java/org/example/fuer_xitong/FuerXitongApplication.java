package org.example.fuer_xitong;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;



//@ComponentScan(basePackages = "org.example")
@MapperScan("org.example.fuer_xitong.mapper")
@SpringBootApplication
public class FuerXitongApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuerXitongApplication.class, args);
    }

}
