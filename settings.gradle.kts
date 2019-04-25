rootProject.name = "aws-sam-gradle"

include(
    ":samples:sample",      // single-module
    ":samples:controllers", // multi-module
    ":samples:controllers:module1",
    ":samples:controllers:module2"
)

includeBuild("./plugin") {
    dependencySubstitution {
        substitute(module("com.importre:aws-sam-gradle-plugin")).with(project(":"))
    }
}
