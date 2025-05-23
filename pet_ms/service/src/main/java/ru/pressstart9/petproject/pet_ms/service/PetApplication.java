package ru.pressstart9.petproject.pet_ms.service;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "ru.pressstart9.petproject.pet_ms")
@EnableJpaRepositories(basePackages = "ru.pressstart9.petproject.pet_ms.dao")
@EntityScan(basePackages = "ru.pressstart9.petproject.pet_ms.domain")
@EnableAsync
public class PetApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(PetApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }
}