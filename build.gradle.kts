import java.util.Properties

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.41")
    }
}

apply(plugin = "org.jetbrains.kotlin.jvm")

plugins {
    // TODO: plugins DSL does not work with multiple project
    // kotlin("jvm")
    application
}

group = "com.kentrino"
version = "0.0.1"

repositories {
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    val ktorVersion = "1.2.3"
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")

    implementation("com.graphql-java-kickstart:graphql-java-tools:5.6.0")

    implementation("org.jetbrains.exposed:exposed:0.14.1")
    implementation("com.zaxxer:HikariCP:3.3.0")
    runtimeOnly("mysql:mysql-connector-java:8.0.14")

    val koinVersion = "2.0.1"
    implementation("org.koin:koin-logger-slf4j:$koinVersion")
    implementation("org.koin:koin-core:$koinVersion")
    implementation("org.koin:koin-ktor:$koinVersion")
    testImplementation("org.koin:koin-test:$koinVersion")

    runtimeOnly("org.slf4j:slf4j-log4j12:1.7.26")

    // http client
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")
    implementation("io.ktor:ktor-client-jackson:$ktorVersion")

    // case conversion
    implementation("com.google.guava:guava:28.0-jre")

    // auth
    implementation("io.ktor:ktor-auth:$ktorVersion")

    // for webhook auth
    implementation("commons-codec:commons-codec:1.10")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.9")
}

application {
    mainClassName = "com.kentrino.Application"
}

tasks.getByName<JavaExec>("run") {
    val propertiesFile = project.rootProject.file("local.properties")
    if (propertiesFile.exists()) {
        Properties().apply {
            load(propertiesFile.inputStream())
            @Suppress("UNCHECKED_CAST")
            systemProperties(this as Map<String, String>)
        }
    }
}
