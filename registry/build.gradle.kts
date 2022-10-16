import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.1"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    id("org.liquibase.gradle") version "2.0.4"
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
    id("maven-publish")
}

group "com.jetbrains"
version "1.0"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
//    kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

//    spring dependecies
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.springframework:spring-context-support:5.3.21")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//    client
    implementation("commons-io:commons-io:2.11.0")
//    compression lib
    implementation("org.apache.commons:commons-compress:1.21")

//    DB
    implementation("org.postgresql:postgresql:42.5.0")

//    liquibase
    implementation("org.liquibase:liquibase-core:4.16.1")

//    quartz dependency
    implementation("org.quartz-scheduler:quartz:2.3.2")
//    tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

//tasks {
//    test {
//        useJUnitPlatform()
//        testLogging {
//            events("failed", "passed", "started")
//        }
//    }
//}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}


