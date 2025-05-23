plugins {
    java
}

dependencies {
    implementation(project(":commons"))

    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
}