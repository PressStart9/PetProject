dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(platform("org.hibernate.orm:hibernate-platform:6.6.9.Final"))
    implementation("org.hibernate.orm:hibernate-core")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")

    implementation("org.postgresql:postgresql:42.7.5")

    testImplementation("org.testcontainers:testcontainers:1.20.6")

    implementation(platform("org.testcontainers:testcontainers-bom:1.20.6"))
    testImplementation("org.testcontainers:postgresql")

    testImplementation("ch.qos.logback:logback-classic:1.2.11")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runApplication") {
    mainClass.set("org.example.DbApp")
    classpath = sourceSets.main.get().runtimeClasspath
}

tasks.register<Exec>("dockerCompose") {
    commandLine("docker-compose", "up", "--build")

    environment["POSTGRES_DB"] = System.getenv("POSTGRES_DB")
    environment["POSTGRES_PORT"] = System.getenv("POSTGRES_PORT")
    environment["POSTGRES_PASSWORD"] = System.getenv("POSTGRES_PASSWORD")
    environment["POSTGRES_USER"] = System.getenv("POSTGRES_USER")

    environment["PGADMIN_DEFAULT_PASSWORD"] = System.getenv("PGADMIN_DEFAULT_PASSWORD")
    environment["PGADMIN_DEFAULT_EMAIL"] = System.getenv("PGADMIN_DEFAULT_EMAIL")
}