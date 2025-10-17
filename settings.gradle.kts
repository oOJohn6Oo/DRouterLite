pluginManagement {
    repositories {
        mavenLocal()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}
includeBuild("plugin-assembler")
includeBuild("plugin-common-module")

rootProject.name = "DRouterLite"

include(":app")
include(":drouter-api")
include(":plugin-collector")
include(":drouter-api-stub")
include(":drouter-api-annotation")
include(":out:mylibrary")
include(":out:mylibrary2")
