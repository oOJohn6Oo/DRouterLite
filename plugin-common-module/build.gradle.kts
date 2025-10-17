import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.kotlin.stdlib)
    compileOnly(libs.gradle)
    //添加Gradle相关的API，否则无法自定义Plugin和Task
    implementation(gradleApi())
    implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        create("localModuleCommon") {
            id = "localModuleCommonPlugin"
            implementationClass = "LocalModuleCommonPlugin"
        }
    }
}