package io.john6.router.drouterlite.pluginassembler

import com.android.build.api.artifact.ScopedArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ScopedArtifacts
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class AssembleRouterByAddingSourcePlugin : Plugin<Project> {
    override fun apply(project: Project) {

        val haveCollectorModules = mutableSetOf<String>()

        project.plugins.withType(AppPlugin::class.java) {

            // Check all sub project's dependencies
            project.rootProject.subprojects.forEach { subP ->
                if (subP.state.executed) {
                    subP.queryDependencies { haveCollectorModules.add(it) }
                } else {
                    subP.afterEvaluate {
                        it.queryDependencies { name -> haveCollectorModules.add(name) }
                    }
                }
            }

            // Set a task when artifacts build
            val androidComponents =
                project.extensions.getByType(AndroidComponentsExtension::class.java)
            androidComponents.onVariants { variant ->

                val variantName = variant.name

                val taskName = "${variantName.cap()}DRouterLiteAssembleTask"
                val taskProvider = project.tasks.register(
                    taskName,
                    AssembleRouterByAddingSourceTask::class.java,
                    haveCollectorModules,
                )

//                NOT WORK when just generate .kt files
                variant.artifacts
                    .forScope(ScopedArtifacts.Scope.PROJECT)
                    .use(taskProvider)
                    .toAppend(ScopedArtifact.CLASSES,  AssembleRouterByAddingSourceTask::generatedDir)
            }
        }

    }


    private fun String.cap() = this.replaceFirstChar { it.uppercase() }

    fun logV(msg: Any) {
        println("\u001b[36m$msg\u001b[0m")
    }
}