package ru.pressstart9.petproject.api_ms.presentation

-microservice.presentation;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "ru.pressstart9.petproject.presentation", "ru.pressstart9.petproject.presentation.security" })
public class TestApplication {
}
