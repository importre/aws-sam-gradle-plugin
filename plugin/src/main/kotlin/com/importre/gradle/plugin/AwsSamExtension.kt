package com.importre.gradle.plugin

import java.io.File

open class AwsSamExtension {
    var bucket = ""
    var stack = ""
    var template: File = File("template.yml")
    var updateCliApps = false

    companion object {
        const val name = "sam"
    }
}
