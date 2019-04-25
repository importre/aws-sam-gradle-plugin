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

apply<AwsSamPlugin>()

configure<AwsSamExtension> {
    template = File(projectDir, "template.yml")
}
