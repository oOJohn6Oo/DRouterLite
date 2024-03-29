package io.john6.router.drouterlite.pluginassembler

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized

class AssembleRouterByAddingSourcePlugin : Plugin<Project> {
    override fun apply(project: Project) {

        project.plugins.withType(AppPlugin::class.java) {

            val dRouterLiteSetting = project.extensions.create("DRouterLite", DRouterLiteSetting::class.java)

            val androidComponents =
                project.extensions.getByType(AndroidComponentsExtension::class.java)
            androidComponents.onVariants { variant ->


                val availableModuleNameSet = getAllAvailableModuleName(project, dRouterLiteSetting)

                logV("availableModuleNameList: ${availableModuleNameSet.joinToString()}")

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

    private fun getAllAvailableModuleName(
        project: Project,
        dRouterLiteSetting: DRouterLiteSetting
    ): Set<String> {
        val allModuleNameSet = dRouterLiteSetting.allModuleName.ifEmpty {
            project.rootProject.subprojects.filter {
                it.buildFile.exists()
            }.map { it.name } + dRouterLiteSetting.includeModuleName - dRouterLiteSetting.excludeModuleName
        }
        return allModuleNameSet.map { getModuleNameFromFile(it).uppercase() }.toSet()
    }

    private fun getModuleNameFromFile(name: String): String {
        return try {
            name.filter { it in '0'..'9' || it in 'A'..'Z' || it in 'a'..'z' }
        } catch (e: Exception) {
//            emit("failed to get module name: ${e.message}")
            ""
        }
    }

    fun logV(msg: Any) {
        println("\u001b[36m$msg\u001b[0m")
    }
}