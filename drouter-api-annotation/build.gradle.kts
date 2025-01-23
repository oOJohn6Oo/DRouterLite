import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.dokka)
    alias(libs.plugins.maven.publish)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>{
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

mavenPublishing {
    coordinates("io.github.oojohn6oo", "drouterlite-annotation", "1.0.0-alpha01")
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    pom {
        name.set("DRouterLite-API-Annotation")
        description.set("Annotation for DRouterLite")
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