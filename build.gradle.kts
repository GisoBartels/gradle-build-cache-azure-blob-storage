import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream

plugins {
    kotlin("jvm") version "1.3.72"
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.squareup.okhttp3:okhttp:4.7.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.giso"
            artifactId = "gradle-build-cache-azure-blob-storage"
            version = gitVersion

            from(components["java"])
        }
    }
}

val gitVersion: String
    get() = exec("git", "describe", "--tags", "--match", "v*", "--dirty", "--always")

fun exec(vararg commandLine: String): String = ByteArrayOutputStream().also { output ->
    exec {
        commandLine(*commandLine)
        standardOutput = output
    }
}.toString().trim()

tasks.register("currentVersion") {
    doLast {
        println("Current version: $gitVersion")
    }
}
