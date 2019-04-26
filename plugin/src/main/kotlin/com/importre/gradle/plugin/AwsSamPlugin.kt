package com.importre.gradle.plugin

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.getValue
import java.io.File

class AwsSamPlugin : Plugin<Project> {

    private val groupName = "aws.sam"

    override fun apply(target: Project): Unit = target.run {
        val sam = extensions.create<AwsSamExtension>(AwsSamExtension.name)
        val generatedDir = File(buildDir, "generated")
        val packageFile = File(generatedDir, "package.yaml")

        allprojects.forEach { project ->
            project.pluginManager.apply("java")
            project.pluginManager.apply("com.github.johnrengelman.shadow")
            val build = project.tasks.getByName("build")
            val shadow = project.tasks.getByName("shadowJar", ShadowJar::class)
            shadow.initShadowJar(build)
        }

        val installAws by tasks.register("installAws", Exec::class.java) {
            val cmd = listOf("pip", "install", "awscli", "--upgrade", "--user")
            group = groupName
            description = cmd.joinToString("")
            workingDir = rootDir
            commandLine = cmd
        }

        val installSam by tasks.register("installSam", Exec::class.java) {
            val cmd = listOf("pip", "install", "aws-sam-cli", "--upgrade", "--user")
            group = groupName
            description = cmd.joinToString("")
            workingDir = rootDir
            commandLine = cmd
        }

        val installAwsSam by tasks.register("installAwsSam") {
            group = groupName
            description = "install awscli, aws-sam-cli"
            dependsOn(installAws, installSam)
            installSam.mustRunAfter(installAws)
        }

        val makeBucket by tasks.register("makeBucket", Exec::class.java) {
            group = groupName
            description = "aws s3 mb s3://<YOUR_S3_BUCKET>"
            workingDir = rootDir
            commandLine = listOf("aws", "s3", "mb", "s3://${sam.bucket}")
            isIgnoreExitValue = true
            installAwsSam
                .takeIf { sam.updateCliApps }
                ?.let { dependsOn(it) }
        }

        val packageSamApp by tasks.register("packageSamApp", Exec::class.java) {
            outputs.file(packageFile)
            group = groupName
            description = "sam package ..."
            workingDir = rootDir
            commandLine = listOf(
                "sam", "package",
                "--template-file", sam.template.absolutePath,
                "--s3-bucket", sam.bucket,
                "--output-template-file", packageFile.absolutePath
            )

            dependsOn(*buildTasks)
            dependsOn(makeBucket)
            buildTasks.forEach(makeBucket::mustRunAfter)
        }

        val printOutputs by tasks.register("printOutputs", Exec::class.java) {
            group = groupName
            description = "aws cloudformation describe-stack ..."
            workingDir = rootDir
            commandLine = listOf(
                "aws", "cloudformation", "describe-stacks",
                "--stack-name", sam.stack,
                "--query", "Stacks[0].Outputs"
            )
        }

        tasks.register("deploySamApp", Exec::class.java) {
            group = groupName
            description = "sam deploy ..."
            workingDir = rootDir
            commandLine = listOf(
                "sam", "deploy",
                "--template-file", packageFile.absolutePath,
                "--stack-name", sam.stack,
                "--capabilities", "CAPABILITY_IAM"
            )

            dependsOn(packageSamApp)
            finalizedBy(printOutputs)
        }

        tasks.register("runLocalStartApi", Exec::class.java) {
            group = groupName
            description = "sam local start-api ..."
            workingDir = rootDir
            commandLine = listOf(
                "sam", "local", "start-api",
                "--template", sam.template.absolutePath
            )

            dependsOn(*buildTasks)
            buildTasks.forEach(makeBucket::mustRunAfter)
        }
    }

    private fun ShadowJar.initShadowJar(build: Task) {
        build.finalizedBy(this)
        archiveVersion.set("")
    }

    private val Project.buildTasks
        get() = allprojects
            .mapNotNull { it.tasks.findByName("build") }
            .toTypedArray()
}
