plugins {
    java
    id("org.springframework.boot") version "3.4.4" apply false
    id("io.spring.dependency-management") version "1.1.7"
}

dependencies {
    implementation(project(":petproject-commons"))
    implementation(project(":petproject-dto"))
    implementation(project(":petproject-domain"))
    implementation(project(":petproject-dao"))

    implementation("org.springframework:spring-context:6.2.5")
    implementation("org.springframework.data:spring-data-jpa:3.4.4")

    implementation(platform("org.hibernate.orm:hibernate-platform:6.6.9.Final"))
    implementation("org.hibernate.orm:hibernate-core")

    testImplementation("org.mockito:mockito-core:5.17.0")

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