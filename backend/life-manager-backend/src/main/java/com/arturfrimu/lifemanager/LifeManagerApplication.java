package com.arturfrimu.lifemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
public class LifeManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LifeManagerApplication.class, args);
    }
}
