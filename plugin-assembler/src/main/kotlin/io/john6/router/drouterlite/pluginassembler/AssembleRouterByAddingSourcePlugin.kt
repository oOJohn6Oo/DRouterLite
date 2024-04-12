package io.john6.router.drouterlite.pluginassembler

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.configurationcache.extensions.capitalized

class AssembleRouterByAddingSourcePlugin : Plugin<Project> {
    override fun apply(project: Project) {

        val haveCollectorModuleSet = mutableSetOf<String>()

        project.plugins.withType(AppPlugin::class.java) {

            // Check all sub project's dependencies
            project.rootProject.subprojects.filter { it.buildFile.exists() }.forEach { subP ->
                subP.afterEvaluate {
                    val res = it.configurations.asMap["ksp"]?.dependencies?.find { dep ->
                        dep.matchRemoteCollector() || dep.matchLocalCollector()
                    }
                    if (res != null) {
                        haveCollectorModuleSet.add(subP.name)
                    }
                }
            }

            // Set a task when artifacts build
            val androidComponents =
                project.extensions.getByType(AndroidComponentsExtension::class.java)
            androidComponents.onVariants { variant ->
                val variantName = variant.name

                val taskName = "${variantName.capitalized()}DRouterLiteAssembleTask"
                val taskProvider = project.tasks.register(
                    taskName,
                    AssembleRouterByAddingSourceTask::class.java,
                    project.name,
                    haveCollectorModuleSet,
                    variant.runtimeConfiguration
                )

//                NOT WORK when just generate .kt files
                variant.artifacts
                    .forScope(ScopedArtifacts.Scope.PROJECT)
                    .use(taskProvider)
                    .toAppend(ScopedArtifact.CLASSES,  AssembleRouterByAddingSourceTask::generatedDir)
            }
        }

    }

    private fun Dependency.matchLocalCollector(): Boolean {
        return group?.startsWith("DRouterLite") ?: false && name == "plugin-collector" && version == "unspecified"
    }

    private fun Dependency.matchRemoteCollector(): Boolean {
        return group == "io.github.oojohn6oo" && name == "drouterlite-collector"
    }


    fun logV(msg: Any) {
        println("\u001b[36m$msg\u001b[0m")
    }
}