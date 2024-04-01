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

    val localProperties = Properties()
    localProperties.load(project.rootProject.file("local.properties").inputStream())
    val routerLocalTest = localProperties["drouter_lite_local_test"].toString().toBooleanStrictOrNull() ?: false
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