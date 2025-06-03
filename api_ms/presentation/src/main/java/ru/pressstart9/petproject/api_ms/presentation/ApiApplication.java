package ru.pressstart9.petproject.api_ms.presentation;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Profile("!test")
@SpringBootApplication(scanBasePackages = "ru.pressstart9.petproject.api_ms")
@EnableJpaRepositories(basePackages = "ru.pressstart9.petproject.api_ms.dao")
@EntityScan(basePackages = "ru.pressstart9.petproject.api_ms.domain")
public class ApiApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ApiApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }
}