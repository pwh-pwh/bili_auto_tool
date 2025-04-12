plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.1.0"
}

group = "org.coderpwh"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://maven.aliyun.com/repository/public/")
    maven("https://maven.aliyun.com/repository/central")
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.seleniumhq.selenium:selenium-java:4.27.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.test {
    useJUnitPlatform()
}