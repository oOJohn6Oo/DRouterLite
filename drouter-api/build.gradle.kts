plugins {
    id(libs.plugins.androidLibrary.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
    id("localModuleCommonPlugin")
    alias(libs.plugins.kotlin.dokka)
    alias(libs.plugins.maven.publish)
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
}

dependencies {
    compileOnly(project(":drouter-api-stub"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.startup)


    val dRouterLiteLocalTest: Boolean by rootProject.ext
    if(dRouterLiteLocalTest) {
        println("drouter-Api local")
        api(project(":drouter-api-annotation"))
    }else{
        println("drouter-Api remote")
        api(libs.drouter.annotation)
    }
}

mavenPublishing {
    coordinates("io.github.oojohn6oo", "drouterlite-api", "1.0.0-alpha03")
        pom {
        name.set("DRouterLite-API")
        description.set("API for DRouterLite")
        url.set("https://github.com/oOJohn6Oo/DRouterLite")
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "oOJohn6Oo"
                name = "John6"
                email = "john6.lq@gmail.com"
            }
        }
        scm {
            connection.set("scm:git:git://github.com/oOJohn6Oo/DRouterLite.git")
            developerConnection.set("scm:git:ssh://github.com/oOJohn6Oo/DRouterLite.git")
            url.set("https://github.com/oOJohn6Oo/DRouterLite")
        }
    }
}