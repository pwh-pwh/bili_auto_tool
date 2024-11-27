plugins {
    kotlin("jvm") version "2.0.21"
}

group = "org.coderpwh"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.seleniumhq.selenium:selenium-java:4.27.0")
}

tasks.test {
    useJUnitPlatform()
}