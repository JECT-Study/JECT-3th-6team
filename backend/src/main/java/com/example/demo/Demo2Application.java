package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class Demo2Application {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+09:00"));
        SpringApplication.run(Demo2Application.class, args);
    }
}
