plugins {
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.register<JavaExec>("consoleApp") {
    mainClass.set("org.example.ConsoleApp")
    classpath = sourceSets["main"].runtimeClasspath
    standardInput = System.`in`

    dependsOn(":genDocs")
}

tasks.register<Javadoc>("genDocs") {
    source = sourceSets["main"].allJava
    options.memberLevel = JavadocMemberLevel.PUBLIC
    isFailOnError = false
}