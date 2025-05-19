plugins {
    java
}

dependencies {
    implementation(project(":petproject-commons"))

    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}