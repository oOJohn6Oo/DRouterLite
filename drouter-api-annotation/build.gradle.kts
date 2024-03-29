plugins {
    alias(libs.plugins.kotlin.jvm)
    id("maven-publish")
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_17
    withJavadocJar()
    withSourcesJar()
}

publishing{
    publications {
        create<MavenPublication>("DRouterLiteAPIAnnotation") {
            groupId = "io.john6.router.drouterlite"
            artifactId = "api-annotation"
            version = "1.0.0-alpha01"
            from(components["java"])
        }
    }
    repositories {
        maven("https://jitpack.io")
    }
}