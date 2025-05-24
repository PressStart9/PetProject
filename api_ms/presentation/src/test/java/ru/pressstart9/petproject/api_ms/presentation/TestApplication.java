package ru.pressstart9.petproject.api_ms.presentation;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;

@Profile("test")
@SpringBootApplication
@ComponentScan(basePackages = { "ru.pressstart9.petproject.api_ms.presentation", "ru.pressstart9.petproject.api_ms.presentation.security" })
public class TestApplication {
}
