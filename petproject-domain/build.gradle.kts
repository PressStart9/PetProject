plugins {
    java
}

dependencies {
    implementation(project(":petproject-commons"))

    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
}