plugins {
    kotlin("jvm") version Version.kotlin
}

repositories {
    jcenter()
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(awsLambda("core", Version.AwsLambda.core))
    implementation(awsLambda("events", Version.AwsLambda.events))
    implementation(awsLambda("log4j2", Version.AwsLambda.log4j2))
    testImplementation(kotlin("test-junit"))
}
