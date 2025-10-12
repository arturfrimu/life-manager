package com.arturfrimu.lifemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.arturfrimu.lifemanager")
public class LifeManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LifeManagerApplication.class, args);
    }
}
