import java.io.ByteArrayOutputStream

plugins {
    kotlin("jvm") version "1.8.10"
    `maven-publish`
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(gradleApi())
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
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
    get() = exec("git", "describe", "--tags", "--match", "[0-9].[0-9]*", "--dirty", "--always")

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
