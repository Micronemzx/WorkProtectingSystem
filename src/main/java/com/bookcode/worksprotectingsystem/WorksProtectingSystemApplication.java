package com.bookcode.worksprotectingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.Entity;

@ComponentScan(basePackages = "com.bookcode.worksprotectingsystem.*")
@EnableJpaRepositories(basePackages = "com.bookcode.worksprotectingsystem.Dao")
@EntityScan("com.bookcode.worksprotectingsystem.entity")

@SpringBootApplication
public class WorksProtectingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorksProtectingSystemApplication.class, args);
    }

}
