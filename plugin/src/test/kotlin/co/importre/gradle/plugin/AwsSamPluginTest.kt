package co.importre.gradle.plugin

import com.importre.gradle.plugin.AwsSamExtension
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.invoke
import org.gradle.testfixtures.ProjectBuilder
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class AwsSamPluginTest {

    @Test
    fun testAwsSamPlugin(): Unit = ProjectBuilder.builder().build().run {
        pluginManager.apply("aws.sam")

        val expectedBucket = "sampleBucket"
        val expectedStack = "sampleStack"
        val expectedTemplateFile = File(projectDir, "template.yaml")
        val expectedPackageFile = File(buildDir, "generated/package.yaml")

        configure<AwsSamExtension> {
            bucket = expectedBucket
            stack = expectedStack
            template = expectedTemplateFile
        }

        tasks {
            val installAws = getByName<Exec>("installAws")
            assertEquals(
                expected = "pip install awscli --upgrade --user",
                actual = installAws.commandLine.joinToString(" ")
            )

            val installSam = getByName<Exec>("installSam")
            assertEquals(
                expected = "pip install aws-sam-cli --upgrade --user",
                actual = installSam.commandLine.joinToString(" ")
            )

            val installAwsSam = getByName("installAwsSam")
            assertEquals(
                expected = setOf(installAws, installSam),
                actual = installAwsSam.dependsOn
            )

            val makeBucket = getByName<Exec>("makeBucket")
            assertEquals(
                expected = "aws s3 mb s3://$expectedBucket",
                actual = makeBucket.commandLine.joinToString(" ")
            )

            val build = getByName("build")
            val packageSamApp = getByName<Exec>("packageSamApp")
            assertEquals(
                expected = "sam package " +
                    "--template-file ${expectedTemplateFile.absolutePath} " +
                    "--s3-bucket $expectedBucket " +
                    "--output-template-file $expectedPackageFile",
                actual = packageSamApp.commandLine.joinToString(" ")
            )
            assertEquals(
                expected = listOfNotNull(build, makeBucket).toSet(),
                actual = packageSamApp.dependsOn
            )

            val deploySamApp = getByName<Exec>("deploySamApp")
            assertEquals(
                expected = "sam deploy " +
                    "--template-file $expectedPackageFile " +
                    "--stack-name $expectedStack " +
                    "--capabilities CAPABILITY_IAM",
                actual = deploySamApp.commandLine.joinToString(" ")
            )
            assertEquals(
                expected = setOf(packageSamApp),
                actual = deploySamApp.dependsOn
            )

            val runLocalStartApi = getByName<Exec>("runLocalStartApi")
            assertEquals(
                expected = "sam local start-api --template $expectedTemplateFile",
                actual = runLocalStartApi.commandLine.joinToString(" ")
            )
        }
    }
}
