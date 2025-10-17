import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("unused")
class LocalModuleCommonPlugin: Plugin<Project>{
    override fun apply(project: Project) {
        project.extensions.getByType(BaseExtension::class).run {
            val isApp = this is com.android.build.gradle.AppExtension
            compileSdkVersion(34)
            defaultConfig {
                minSdk = 14
                targetSdk = 34
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                getByName("release"){
                    isMinifyEnabled = isApp
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

            buildFeatures.viewBinding = true

            project.tasks.withType<KotlinCompile>{
                compilerOptions {
                    jvmTarget = JvmTarget.JVM_17
                    apiVersion = KotlinVersion.KOTLIN_2_0 // sourceCompatibility
                    languageVersion = KotlinVersion.KOTLIN_2_0 // targetCompatibility
                }
            }

            project.dependencies{
                add("compileOnly", "org.jetbrains.kotlin:kotlin-stdlib:2.2.20")
                add("testImplementation", "junit:junit:4.13.2")
                add("androidTestImplementation","androidx.test.ext:junit:1.1.5")
                add("androidTestImplementation","androidx.test.espresso:espresso-core:3.5.1")
                add("androidTestImplementation","androidx.test.espresso:espresso-intents:3.4.0")
//                add("androidTestImplementation","androidx.test.espresso:espresso-web:3.4.0")
            }
        }
    }
}