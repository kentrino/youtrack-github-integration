plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.41")
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    val ktorVersion = "1.2.2"
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

    // case conversion
    implementation("com.google.guava:guava:28.0-jre")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClassName = "com.kentrino.AppKt"
}
