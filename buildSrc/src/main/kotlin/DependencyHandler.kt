import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.gson(): Any =
    "com.google.code.gson:gson:${Version.gson}"

fun DependencyHandler.awsLambda(module: String, version: String): Any =
    "com.amazonaws:aws-lambda-java-$module:$version"

fun DependencyHandler.kotlinx(module: String, version: String): Any =
    "org.jetbrains.kotlinx:kotlinx-$module:$version"
