import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_17
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.kotlin.ksp)

    val localProperties = Properties()
    localProperties.load(project.rootProject.file("local.properties").inputStream())
    val routerLocalTest = localProperties["drouter_lite_local_test"].toString().toBooleanStrictOrNull() ?: false
    if(routerLocalTest) {
        implementation(project(":drouter-api-annotation"))
    }else{
        implementation(libs.drouter.annotation)
    }
}

publishing{
    publications {
        create<MavenPublication>("DRouterLiteKSP") {
            groupId = "io.john6.router.drouterlite"
            artifactId = "collector"
            version = "1.0.0-alpha01"
            from(components["java"])
        }
    }
    repositories {
        maven("https://jitpack.io"){
            name = "jitpack"
        }
    }
}