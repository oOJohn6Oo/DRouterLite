import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlin.ksp)
    id(libs.plugins.kotlin.android.get().pluginId)
    // 统一配置插件
    id("localModuleCommonPlugin")
    // 路由组装插件
    alias(libs.plugins.drouter.assembler)
}

android {
    namespace = "io.john6.router.drouterlite"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.john6.router.drouterlite"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

DRouterLite{
    this.excludeModuleName = setOf(
        "drouter-api",
        "drouter-api-stub",
        "drouter-api-annotation",
        "plugin-assembler",
        "plugin-collector",
        "plugin-common-module"
    )
}

dependencies {
    implementation(libs.material)
    runtimeOnly(project(":out:mylibrary"))

    val dRouterLiteLocalTest: Boolean by rootProject.ext
    if(dRouterLiteLocalTest) {
        println("app local")
        // 路由 API
        implementation(project(":drouter-api"))
        // 路由收集插件
        ksp(project(":plugin-collector"))
    }else{
        println("app remote")
        implementation(libs.drouter.api)
        ksp(libs.drouter.collector)
    }

}