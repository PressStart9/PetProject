dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.register<JavaExec>("consoleApp") {
    mainClass.set("org.example.ConsoleApp")
    classpath = sourceSets.main.get().runtimeClasspath

    standardInput = System.`in`

    dependsOn(tasks.named("genDocs"))
}