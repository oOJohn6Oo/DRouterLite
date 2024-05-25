package io.john6.router.drouterlite.plugincollector

import com.google.devtools.ksp.KSTypeNotPresentException
import io.john6.router.drouterlite.annotation.Router
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import io.john6.router.drouterlite.annotation.Service
import java.io.OutputStream

fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}

/**
 * DRouterList 注解处理器
 * @param codeGenerator 用于生成与管理文件，不使用此 API 创建的文件将不会参与增量处理或后续编译。
 * @param options 可以获取build.gradle声明的ksp option
 */
class DRouterLiteSymbolProcessor(
    private val codeGenerator: CodeGenerator,
    private val options: Map<String, String>,
    private val logger: KSPLogger
) : SymbolProcessor {
    private var file: OutputStream? = null
    private var invoked = false

    /**
     * key: path
     * value: class qualifiedName
     */
    private val routerMap = hashMapOf<String, String>()

    /**
     * key: interface qualifiedName
     * value: class qualifiedName
     */
    private val serviceMap = hashMapOf<String, String>()

    private fun emit(s: String, indent: String = "") {
        file?.appendText("$indent$s\n")
//        logger.warn("$indent$s")
    }

    /**
     * Use resolver.getSymbolsWithAnnotation() to get the symbols you want to process, given the fully-qualified name of an annotation.
     *
     * A common use case for KSP is to implement a customized visitor (interface com.google.devtools.ksp.symbol.KSVisitor)
     * for operating on symbols. A simple template visitor is com.google.devtools.ksp.symbol.KSDefaultVisitor.
     *
     *
     */
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked) {
            return emptyList()
        }
        val allFiles = resolver.getAllFiles()
        if (allFiles.none()) {
            return emptyList()
        }
        val ksList = mutableListOf<KSFile>()
        val moduleName = getModuleNameFromFile(allFiles.first()).uppercase()
//        file = codeGenerator.createNewFile(
//            Dependencies(false),
//            "",
//            "${moduleName}MyRouterSymbolProcessor",
//            "log"
//        )
        emit("TestProcessor: init($options)", "")
        invoked = true

        if (moduleName.isEmpty()) return emptyList()

        processRouter(resolver, ksList)
        processService(resolver, ksList)
        writeRouterToFile(ksList, moduleName)
        writeServiceToFile(ksList, moduleName)

        return emptyList()
    }

    @OptIn(KspExperimental::class)
    private fun processRouter(
        resolver: Resolver,
        ksList: MutableList<KSFile>
    ) {
        val clazzAttType =
            resolver.getClassDeclarationByName("android.app.Activity")?.asType(listOf())
                ?: return

        val routerClassFullName = Router::class.qualifiedName ?: return

        resolver.getSymbolsWithAnnotation(routerClassFullName).filter {
            //            it.validate()
            true
        }.forEach {
            val classDeclaration = it as KSClassDeclaration
            try {
                val annotation = it.getAnnotationsByType(Router::class).first()
                val path = annotation.path
                if (path.isBlank()) {
                    emit("path is blank, skip it $it")
                } else {
                    val containingFile = classDeclaration.containingFile
                    if (containingFile != null) {
                        ksList.add(containingFile)
                    }
                    it.accept(DRouterLiteRouterVisitor(file, path, clazzAttType, routerMap), Unit)
                }
            } catch (e: Exception) {
                emit("exception ${e.message}")
            }
        }
    }

    @OptIn(KspExperimental::class)
    private fun processService(resolver: Resolver, ksList: MutableList<KSFile>) {

        val serviceClassFullName = Service::class.qualifiedName ?: return

        resolver.getSymbolsWithAnnotation(serviceClassFullName).filter {
            //            it.validate()
            true
        }.forEach {
            val declaration = it as KSClassDeclaration
            val annotation = it.getAnnotationsByType(Service::class).first()
            // 获取 接口 的全限定名
            val interfaceName = getServiceClazzName(annotation) ?: return@forEach
//            获取 接口 的类型
            val interfaceType = resolver.getClassDeclarationByName(interfaceName)?.asType(listOf())
                ?: return@forEach
            emit("acquire interfaceType Successful")
            val containingFile = declaration.containingFile
            if (containingFile != null) {
                ksList.add(containingFile)
            }
            it.accept(
                DRouterLiteServiceVisitor(file, interfaceName, interfaceType, serviceMap),
                Unit
            )
        }
    }

    private fun writeRouterToFile(
        ksList: MutableList<KSFile>,
        moduleName: String
    ) {

        val fileKt = codeGenerator.createNewFile(
            Dependencies(true, *ksList.toTypedArray()),
            "io.john6.router.drouterlite.stub",
            "${moduleName}RouterLoader",
            "kt"
        )
        fileKt.appendText(
            """
    package io.john6.router.drouterlite.stub
    
    import io.john6.router.drouterlite.api.core.RouterMeta
    import java.util.HashMap
    
    object ${moduleName}RouterLoader {
        @Suppress("UNUSED_PARAMETER", "USELESS_CAST")
        fun loadAll(map: Map<String, Any>) {
    ${
                routerMap.keys.joinToString(System.lineSeparator()) {
                    "        (map as HashMap)[\"$it\"] = RouterMeta(\"$it\", 1, ${routerMap[it]}::class.java)"
                }
            }
        }
    }
    """.trimIndent()

        )
    }

    private fun writeServiceToFile(
        ksList: MutableList<KSFile>,
        moduleName: String
    ) {
        emit("service Dependencies fileName:")
        emit(ksList.joinToString { it.filePath })
        val fileKt = codeGenerator.createNewFile(
            Dependencies(true, *ksList.toTypedArray()),
            "io.john6.router.drouterlite.stub",
            "${moduleName}ServiceLoader",
            "kt"
        )
        fileKt.appendText(
            """
        package io.john6.router.drouterlite.stub
        
        import io.john6.router.drouterlite.api.core.ServiceMeta
        import java.util.HashMap
        
        object ${moduleName}ServiceLoader {
            @Suppress("UNUSED_PARAMETER", "USELESS_CAST")
            fun loadAll(map: Map<Class<*>, Any>) {
        ${
                serviceMap.keys.joinToString(System.lineSeparator()) {
                    val clazz = serviceMap[it]
                    "        (map as HashMap)[${it}::class.java] =\n" +
                            "                object : \n" +
                            "                     ServiceMeta<$clazz>(${clazz}::class.java){\n" +
                            "                     override fun newInstance() = ${serviceMap[it]}()\n" +
                            "                }"
                }
            }
            }
        }
        """.trimIndent()

        )
    }

    /**
     * 从注解中获取接口的全限定名
     * FIXME [issue1129](https://github.com/google/ksp/issues/1129)
     */
    @OptIn(KspExperimental::class)
    private fun getServiceClazzName(annotation: Service) = try {
        annotation.clazz.qualifiedName
    } catch (e: KSTypeNotPresentException) {
        val s = e.ksType.declaration
        if (s is KSClassDeclaration) {
            s.qualifiedName?.asString()
        } else {
            null
        }?.let { desireName ->
            emit("interfaceName: $desireName")
            desireName
        }
    }


    private fun getModuleNameFromFile(file: KSFile): String {
        return try {
            file.filePath.split("/src/main")[0].split("/").last()
                .filter { it in '0'..'9' || it in 'A'..'Z' || it in 'a'..'z' }
        } catch (e: Exception) {
            emit("failed to get module name: ${e.message}")
            ""
        }
    }
}