plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
    id("org.springframework.boot") version "2.7.1"
    id("io.spring.dependency-management") version "1.0.14.RELEASE"
    id("org.openapi.generator") version "5.3.0"
    id("maven-publish")
}

group = "com.jetbrains.productregistry"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}