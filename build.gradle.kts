val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project

val config4kVersion: String by project
val kodeinVersion: String by project

val databaseVersion: String by project
val flywayVersion: String by project
val exposedVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.5.20"
    kotlin("plugin.serialization") version "1.4.21"
}

group = "com.remcoil"
version = "0.0.1"

//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//}

application {
    mainClass.set("com.remcoil.ApplicationKt")
}


//application {
//    mainClass.set("io.ktor.server.netty.EngineMain")
//}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")

    implementation("io.github.config4k:config4k:$config4kVersion")
    implementation("org.kodein.di:kodein-di-framework-ktor-server-jvm:$kodeinVersion")

    implementation("com.h2database:h2:$databaseVersion")
    implementation("org.flywaydb:flyway-core:$flywayVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}