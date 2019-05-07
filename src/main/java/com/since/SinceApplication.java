package com.since;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.since.dao")
public class SinceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SinceApplication.class, args);
    }

}
