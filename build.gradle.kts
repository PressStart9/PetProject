plugins {
    id("java")
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

allprojects {
    group = "ru.pressstart9"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")

    runtimeOnly("org.postgresql:postgresql")
}

tasks.test {
    useJUnitPlatform()
}