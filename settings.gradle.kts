rootProject.name = "aws-sam-gradle"

include(":sample")

includeBuild("./plugin") {
    dependencySubstitution {
        substitute(module("com.importre:aws-sam-gradle-plugin")).with(project(":"))
    }
}
