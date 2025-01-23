import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

repositories {
    mavenCentral()
    google()
}
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gradle.publish)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies{

    compileOnly(libs.gradle)
    //添加Gradle相关的API，否则无法自定义Plugin和Task
    implementation(gradleApi())
    implementation(libs.asm)
}

group = "io.github.oojohn6oo"
version = "1.0.0-alpha07"

gradlePlugin {
    website.set("https://github.com/oOJohn6Oo/DRouterLite")
    vcsUrl.set("https://github.com/oOJohn6Oo/DRouterLite")
    plugins {
        create("DRouterLiteAssemble") {
            // 仅影响本地模块依赖时的名字以及使用 java-gradle-plugin 插件发布时有影响
            // 具体参考 https://docs.gradle.org/current/userguide/plugins.html#sec:plugin_markers
            id = "io.github.oojohn6oo.drouterlite.assembler"
            implementationClass = "io.john6.router.drouterlite.pluginassembler.AssembleRouterByAddingSourcePlugin"
            displayName = "DRouterLite assemble router plugin"
            description = "Gradle plugin to assemble routers and services in each module"
            tags.set(listOf("android", "router", "android-modularization"))
        }
    }
}