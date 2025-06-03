package ru.pressstart9.petproject.person_ms.service;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"ru.pressstart9.petproject.person_ms"})
@EnableJpaRepositories(basePackages = "ru.pressstart9.petproject.person_ms.dao")
@EntityScan(basePackages = "ru.pressstart9.petproject.person_ms.domain")
@EnableAsync
public class PersonApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(PersonApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }
}