plugins {
    java
    id("org.springframework.boot") version "3.4.4" apply false
    id("io.spring.dependency-management") version "1.1.7"
}

dependencies {
    implementation(project(":commons"))
    implementation(project(":person_ms:domain"))

    implementation("org.springframework.boot:spring-boot")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation(platform("org.hibernate.orm:hibernate-platform:6.6.9.Final"))
    implementation("org.hibernate.orm:hibernate-core")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation(platform("org.testcontainers:testcontainers-bom:1.20.6"))
    testImplementation("org.testcontainers:junit-jupiter:1.20.6")
    testImplementation("org.testcontainers:postgresql:1.20.6")
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

tasks.test {
    useJUnitPlatform()
}