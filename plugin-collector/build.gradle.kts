import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.dokka)
    alias(libs.plugins.maven.publish)
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

dependencies {
    implementation(libs.kotlin.ksp)
    val dRouterLiteLocalTest: Boolean by rootProject.ext
    if (dRouterLiteLocalTest) {
        implementation(project(":drouter-api-annotation"))
    } else {
        implementation(libs.drouter.annotation)
    }
}

mavenPublishing {
    coordinates("io.github.oojohn6oo", "drouterlite-collector", "1.0.0-alpha04")
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    pom {
        name.set("DRouterLite-Collector")
        description.set("Android Router collector for DRouterLite using ksp")
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