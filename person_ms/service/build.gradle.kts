plugins {
    java
    id("org.springframework.boot") version "3.4.4" apply false
    id("io.spring.dependency-management") version "1.1.7"
}

dependencies {
    implementation(project(":commons"))
    implementation(project(":dto"))
    implementation(project(":person_ms:domain"))
    implementation(project(":person_ms:dao"))

    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    implementation("org.springframework.boot:spring-boot")
    implementation("org.springframework:spring-context:6.2.5")
    implementation("org.springframework.data:spring-data-jpa:3.4.4")

    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation(platform("org.hibernate.orm:hibernate-platform:6.6.9.Final"))
    implementation("org.hibernate.orm:hibernate-core")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

tasks.test {
    useJUnitPlatform()
}