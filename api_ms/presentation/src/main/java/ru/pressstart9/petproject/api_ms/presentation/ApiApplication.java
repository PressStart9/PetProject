package ru.pressstart9.petproject.api_ms.presentation;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

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