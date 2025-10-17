import org.jetbrains.kotlin.konan.properties.Properties

val localProperties = Properties()
localProperties.load(project.rootProject.file("local.properties").inputStream())
val localTestFlag = localProperties["dRouterLiteLocalTest"].toString().toBooleanStrictOrNull() ?: false

project.ext["dRouterLiteLocalTest"] = localTestFlag

val dRouterLiteLocalTest:Boolean by project.ext

println("dRouterLiteLocalTest $dRouterLiteLocalTest")

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.maven.publish) apply false
    alias(libs.plugins.kotlin.dokka) apply false
}


subprojects {
    configurations.all {
        resolutionStrategy{
            force(libs.kotlin.stdlib)
        }
    }
}
