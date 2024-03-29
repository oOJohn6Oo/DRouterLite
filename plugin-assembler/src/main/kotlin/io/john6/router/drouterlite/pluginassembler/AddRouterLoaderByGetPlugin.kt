//package io.john6.router.drouterlite.pluginassembler
//
//import com.android.build.api.artifact.ScopedArtifact
//import com.android.build.api.variant.ApplicationAndroidComponentsExtension
//import com.android.build.api.variant.ScopedArtifacts
//import com.android.build.gradle.AppPlugin
//import org.gradle.api.Plugin
//import org.gradle.api.Project
//import org.gradle.configurationcache.extensions.capitalized
//
//class AddRouterLoaderByGetPlugin : Plugin<Project> {
//    override fun apply(project: Project) {
//
//        project.plugins.withType(AppPlugin::class.java) {
//
//            val androidComponents =
//                project.extensions.getByType(ApplicationAndroidComponentsExtension::class.java)
//            androidComponents.onVariants { variant ->
//                val taskName = "${variant.name.capitalized()}AddRouterLoaderByGetTask"
//
//
//                logV("taskName: $taskName")
//
//                val taskProvider =
//                    project.tasks.register(taskName, AddRouterLoaderByGetTask::class.java)
//
//                taskProvider.configure {
//                    variant.artifacts
//                        .forScope(ScopedArtifacts.Scope.PROJECT)
//                        .use(taskProvider)
//                        .toGet(
//                            ScopedArtifact.CLASSES,
//                            AddRouterLoaderByGetTask::allJars,
//                            AddRouterLoaderByGetTask::allDirectories,
//                        )
//                }
//
////                目前不支持这种方式，因为 Gradle Task 倾向于线性的输入输出
////                variant.artifacts
////                    .forScope(ScopedArtifacts.Scope.PROJECT)
////                    .use(taskProvider)
////                    .toGet(
////                        ScopedArtifact.CLASSES,
////                        AddRouterLoaderByGetTask::allJars,
////                        AddRouterLoaderByGetTask::allDirectories,
////                    )
//
////                val taskProvider2 = project.tasks.register(
////                    "${variant.name.capitalized()}AddRouterLoaderByAddingSourceTask2",
////                    AddRouterLoaderByGetTask2::class.java
////                )
//
////                variant.artifacts.forScope(ScopedArtifacts.Scope.PROJECT)
////                    .use(taskProvider2)
////                    .toAppend(ScopedArtifact.CLASSES, AddRouterLoaderByGetTask2::output)
//
////                variant.artifacts.use(taskProvider2)
////                    .wired
////                    .toAppend(ScopedArtifact.CLASSES)
//
////            8.2 deprecated
////            variant.artifacts.use(taskProvider)
////                .wiredWith(MyRouterCollectorTask::generatedDir)
////                .toAppendTo(MultipleArtifact)
//            }
//        }
//
//    }
//
//    fun logV(msg: Any) {
//        println("\u001b[36m$msg\u001b[0m")
//    }
//}