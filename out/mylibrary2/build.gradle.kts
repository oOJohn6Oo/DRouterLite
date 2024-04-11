import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)

    // 统一配置插件
    id("localModuleCommonPlugin")
}

android {
    namespace = "com.example.mylibrary2"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.material)

    val dRouterLiteLocalTest: Boolean by rootProject.ext
    if(dRouterLiteLocalTest) {
        println("mylib2 local")
        // 路由 API
        implementation(project(":drouter-api"))
        // 路由收集插件
        ksp(project(":plugin-collector"))
    }else{
        println("mylib2 remote")
        implementation(libs.drouter.api)
        ksp(libs.drouter.collector)
    }
}