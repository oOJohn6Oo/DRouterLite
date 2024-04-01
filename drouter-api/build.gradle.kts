import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id("localModuleCommonPlugin")
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_17
}

android {
    namespace = "io.john6.router.drouterlite.api"

    defaultConfig {
        minSdk = 11
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = false
    }

    publishing {
        singleVariant("release"){
            withJavadocJar()
            withSourcesJar()
        }
    }
}

dependencies {
    compileOnly(project(":drouter-api-stub"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.startup)


    val localProperties = Properties()
    localProperties.load(project.rootProject.file("local.properties").inputStream())
    val routerLocalTest = localProperties["drouter_lite_local_test"].toString().toBooleanStrictOrNull() ?: false

    if(routerLocalTest) {
        api(project(":drouter-api-annotation"))
    }else{
        api(libs.drouter.annotation)
    }
}

afterEvaluate {
    publishing{
        publications {
            create<MavenPublication>("DRouterLiteAPI") {
                groupId = "io.john6.router.drouterlite"
                artifactId = "api"
                version = "1.0.0-alpha01"
                from(components["release"])
            }
        }
        repositories {
            maven("https://jitpack.io"){
                name = "jitpack"
            }
        }
    }
}