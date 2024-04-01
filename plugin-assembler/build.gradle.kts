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
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies{

    compileOnly(libs.gradle)
    //添加Gradle相关的API，否则无法自定义Plugin和Task
    implementation(gradleApi())
    implementation(libs.asm)
}

group = "io.john6.router.drouterlite"
version = "1.0.0-alpha01"

gradlePlugin {
    website.set("https://github.com/oOJohn6Oo/DRouterLite")
    vcsUrl.set("https://github.com/oOJohn6Oo/DRouterLite")
    plugins {
        create("DRouterLiteAssemble") {
            // 仅影响本地模块依赖时的名字以及使用 java-gradle-plugin 插件发布时有影响
            // 具体参考 https://docs.gradle.org/current/userguide/plugins.html#sec:plugin_markers
            id = "io.john6.router.drouterlite.assembler"
            implementationClass = "io.john6.router.drouterlite.pluginassembler.AssembleRouterByAddingSourcePlugin"
            displayName = "io.john6.router.drouterlite.assembler.gradle.plugin"
            description = "gradle plugin to assemble router stuff for DRouterLite"
            tags.set(listOf("android", "router", "android-modularization"))
        }
    }
}