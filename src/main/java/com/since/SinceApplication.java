package com.since;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 使用自带的tomcat
 */
@SpringBootApplication
@MapperScan("com.since.dao")
@ServletComponentScan
public class SinceApplication {

    public static void main(String[] args) {
        System.setProperty("Log4jContextSelector",
                "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
        SpringApplication.run(SinceApplication.class, args);
    }

}

/**
 * 使用外部tomcat
 */
//@SpringBootApplication
//@MapperScan("com.since.dao")
//@ServletComponentScan
//public class SinceApplication extends SpringBootServletInitializer {
//
//    public static void main(String[] args) {
//        System.setProperty("Log4jContextSelector",
//                "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
//        SpringApplication.run(SinceApplication.class, args);
//    }
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(SinceApplication.class);
//    }
//
//}


