import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `maven-publish`
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.10.1"
}

repositories {
    jcenter()
}

dependencies {
    api("com.github.jengelman.gradle.plugins:shadow:5.0.0")
    testImplementation(gradleTestKit())
    testImplementation(kotlin("test-junit"))
}

group = "com.importre"
version = "0.2.1"

gradlePlugin {
    plugins {
        register("AwsSamPlugin") {
            id = "aws.sam"
            implementationClass = "com.importre.gradle.plugin.AwsSamPlugin"
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

pluginBundle {
    website = "http://github.com/importre/aws-sam-gradle-plugin"
    vcsUrl = "http://github.com/importre/aws-sam-gradle-plugin.git"

    description = "Binding AWS SAM"

    (plugins) {
        "AwsSamPlugin" {
            displayName = "AwsSamPlugin"
            tags = listOf("aws", "sam", "cli")
            version = "${project.version}"
        }
    }
}
