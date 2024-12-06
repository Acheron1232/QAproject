package com.zxc.naruto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class NarutoApplication {

    public static void main(String[] args) {
        SpringApplication.run(NarutoApplication.class, args);
    }

}
