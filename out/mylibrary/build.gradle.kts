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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    val localProperties = Properties()
    localProperties.load(project.rootProject.file("local.properties").inputStream())
    val routerLocalTest = localProperties["drouter_lite_local_test"].toString().toBooleanStrictOrNull() ?: true

    implementation(libs.material)
    if(routerLocalTest) {
        // 路由 API
        implementation(project(":drouter-api"))
        // 路由收集插件
        ksp(project(":plugin-collector"))
    }else{
        implementation(libs.drouter.api)
        ksp(libs.drouter.collector)
    }
}