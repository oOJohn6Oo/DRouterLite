import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)

    // 统一配置插件
    id("localModuleCommonPlugin")
}

android {
    namespace = "com.example.mylibrary"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {

    implementation(libs.material)
    implementation(project(":out:mylibrary2"))

    val dRouterLiteLocalTest: Boolean by rootProject.ext
    if(dRouterLiteLocalTest) {
        println("mylib local")
        // 路由 API
        implementation(project(":drouter-api"))
        // 路由收集插件
        ksp(project(":plugin-collector"))
    }else{
        println("mylib remote")
        implementation(libs.drouter.api)
        ksp(libs.drouter.collector)
    }
}