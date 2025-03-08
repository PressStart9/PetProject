dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(platform("org.hibernate.orm:hibernate-platform:6.6.9.Final"))
    implementation("org.hibernate.orm:hibernate-core")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}