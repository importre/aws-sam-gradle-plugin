import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.importre.gradle.plugin.AwsSamExtension
import com.importre.gradle.plugin.AwsSamPlugin

buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath("com.importre:aws-sam-gradle-plugin")
    }
}

plugins {
    kotlin("jvm") version Version.kotlin
}

group = "com.importre.example"
version = "0.0.0"

apply<AwsSamPlugin>()

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(gson())
    implementation(awsLambda("core", Version.AwsLambda.core))
    implementation(awsLambda("events", Version.AwsLambda.events))
    implementation(awsLambda("log4j2", Version.AwsLambda.log4j2))
    implementation(kotlinx("html-jvm", Version.KotlinX.html))
    testImplementation(kotlin("test-junit"))
}

val bucketName: String by project
val stackName: String by project

configure<AwsSamExtension> {
    bucket = bucketName
    stack = stackName
    template = File(projectDir, "template.yaml")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<ShadowJar> {
        minimize {
            exclude(dependency("joda-time:.*:.*"))
        }
    }
}
