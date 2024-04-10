package io.john6.router.drouterlite.pluginassembler

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.configurationcache.extensions.capitalized

class AssembleRouterByAddingSourcePlugin : Plugin<Project> {
    override fun apply(project: Project) {

        val availableModuleNameSet = mutableSetOf<String>()

        project.plugins.withType(AppPlugin::class.java) {

            // Check all sub project's dependencies
            project.rootProject.subprojects.filter { it.buildFile.exists() }.forEach { subP->
                try {
                    subP.afterEvaluate {
                        val name = getModuleNameFromFile(subP.name).uppercase()
                        // TODO All more logic to handle all conditions, like "kspTest"
                        val dep = subP.configurations.asMap["ksp"]?.dependencies
                        dep?.forEach {
                            if (it.checkIfMatchLocalCollector() || it.checkIfMatchRemoteCollector()) {
                                availableModuleNameSet.add(name)
                            }
                        }
                    }
                } catch (e: Exception) {
                    logV("error: ${e.message} on project named '${subP.name}'")
                }
            }

            // Set a task when artifacts build
            val androidComponents =
                project.extensions.getByType(AndroidComponentsExtension::class.java)
            androidComponents.onVariants { variant ->

                val taskName = "${variant.name.capitalized()}DRouterLiteAssembleTask"
                val taskProvider = project.tasks.register(
                    taskName,
                    AssembleRouterByAddingSourceTask::class.java,
                    availableModuleNameSet,
                )

//                NOT WORK when just generate .kt files
                variant.artifacts
                    .forScope(ScopedArtifacts.Scope.PROJECT)
                    .use(taskProvider)
                    .toAppend(ScopedArtifact.CLASSES,  AssembleRouterByAddingSourceTask::generatedDir)
            }
        }

    }

    private fun Dependency.checkIfMatchLocalCollector(): Boolean {
        return group == "DRouterLite" && name == "plugin-collector" && version == "unspecified"
    }

    private fun Dependency.checkIfMatchRemoteCollector(): Boolean {
        return group == "io.github.oojohn6oo" && name == "drouterlite-collector"
    }

    private fun getModuleNameFromFile(name: String): String {
        return try {
            name.filter { it in '0'..'9' || it in 'A'..'Z' || it in 'a'..'z' }
        } catch (e: Exception) {
            logV("failed to get module name: ${e.message}")
            ""
        }
    }

    fun logV(msg: Any) {
        println("\u001b[36m$msg\u001b[0m")
    }
}