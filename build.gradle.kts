plugins {
    java
}

allprojects {
    plugins.apply("java")

    group = "org.example"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks.register<Javadoc>("genDocs") {
        source = sourceSets.main.get().allJava
        options.memberLevel = JavadocMemberLevel.PUBLIC
        options.encoding = "UTF-8"
        isFailOnError = false
        classpath = files(sourceSets.main.get().compileClasspath, sourceSets.main.get().runtimeClasspath)
    }

    tasks.build {
        dependsOn(tasks.named("genDocs"))
    }
}
