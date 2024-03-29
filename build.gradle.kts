import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    application
}

group = "me.monicaericasardena"
version = "1.2.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:3.4.2")
    implementation("junit:junit:4.13.2")
    implementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation(kotlin("test"))
    testImplementation("junit:junit:4.13")
}

tasks.test {
    useJUnit()
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}
