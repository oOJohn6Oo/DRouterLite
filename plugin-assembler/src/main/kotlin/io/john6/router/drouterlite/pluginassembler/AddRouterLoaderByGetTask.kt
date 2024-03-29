//package io.john6.router.drouterlite.pluginassembler
//
//import com.squareup.kotlinpoet.ClassName
//import com.squareup.kotlinpoet.FileSpec
//import com.squareup.kotlinpoet.FunSpec
//import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
//import com.squareup.kotlinpoet.TypeSpec
//import com.squareup.kotlinpoet.WildcardTypeName
//import com.squareup.kotlinpoet.asTypeName
//import org.gradle.api.DefaultTask
//import org.gradle.api.file.Directory
//import org.gradle.api.file.DirectoryProperty
//import org.gradle.api.file.RegularFile
//import org.gradle.api.provider.ListProperty
//import org.gradle.api.tasks.InputFiles
//import org.gradle.api.tasks.OutputDirectory
//import org.gradle.api.tasks.TaskAction
//
//// 1. 使用 toAppend, Poet 等工具生成 RouterLoader 聚合每个模块中的 Loader
//// 2. dsdf
//abstract class AddRouterLoaderByGetTask : DefaultTask() {
//
//    // In order for the task to be up-to-date when the inputs have not changed,
//    // the task must declare an output, even if it's not used. Tasks with no
//    // output are always run regardless of whether the inputs changed
//    @get:OutputDirectory
//    abstract val output: DirectoryProperty
//
//    // This property will be set to all Jar files available in scope
//    @get:InputFiles
//    abstract val allJars: ListProperty<RegularFile>
//
//    // Gradle will set this property with all class directories that available in scope
//    @get:InputFiles
//    abstract val allDirectories: ListProperty<Directory>
//
//    @TaskAction
//    fun taskAction() {
//
//        logV("projectDirectories.get().size:${allDirectories.get().size}")
//        logV("projectJars.get().size:${allJars.get().size}")
//
//        allJars.get().forEach {
//            logV(it.asFile.name)
//        }
//
//        logV("------------- ${output.get().asFile.absolutePath}")
//
//        val listOfRouterClassName = allDirectories.get().flatMap {dir-> dir.asFile.walk().filter {
//            logV(it.name)
//            it.nameWithoutExtension.contains("RouterLoader")
//        } }.map { it.nameWithoutExtension }
//        logV("listOfRouterClassName: ${listOfRouterClassName.joinToString()}")
//
//        val mapType = ClassName("java.util", "HashMap")
//            .parameterizedBy(
//                String::class.asTypeName(),
//                ClassName("java.lang", "Class").parameterizedBy(
//                    WildcardTypeName.producerOf(Any::class)
//                )
//            )
//
//        val file = FileSpec.builder("io.john6.demo.myrouterstub", "RouterLoader")
//            .addType(
//                TypeSpec.classBuilder("RouterLoader")
//                    .primaryConstructor(FunSpec.constructorBuilder().build())
//                    .addFunction(
//                        FunSpec.builder("loadAll")
//                            .addParameter("map", mapType)
//                            .let {
//                                listOfRouterClassName.forEach { moduleClass ->
//                                    it.addStatement("  try {")
//                                    it.addStatement("    ${moduleClass}.loadAll(map)")
//                                    it.addStatement(
//                                        "  } catch (e: %T) {",
//                                        Exception::class.asTypeName()
//                                    )
//                                    it.addStatement("  }")
//                                }
//                                it
//                            }
//                            .build()
//                    )
//                    .build()
//            )
//            .build()
//        file.writeTo(output.get().asFile)
//    }
//
//
//    fun logV(msg: Any) {
//        println("\u001b[36m$msg\u001b[0m")
//    }
//}