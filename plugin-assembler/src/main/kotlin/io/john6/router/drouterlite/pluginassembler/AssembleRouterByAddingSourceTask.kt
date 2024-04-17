package io.john6.router.drouterlite.pluginassembler

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectories
import org.gradle.api.tasks.TaskAction
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

abstract class AssembleRouterByAddingSourceTask @Inject constructor(
    private val haveCollectorModules: Set<String>,
    private val involvedInThisCompileModules:Set<String>
) :
    DefaultTask() {

    @get:OutputDirectories
    abstract val generatedDir: DirectoryProperty

    @TaskAction
    fun taskAction() {
        logV("output dir path is ${generatedDir.get().asFile.absolutePath}")

        logV(System.lineSeparator())
        logV("haveCollectorModuleSet")
        logV(involvedInThisCompileModules.joinToString())

        logV(System.lineSeparator())
        logV("all modules involves in this compile")
        logV(haveCollectorModules.joinToString())


        val finalModuleList = involvedInThisCompileModules.filter {
            haveCollectorModules.contains(it)
        }.map { getModuleNameFromFile(it) }

        logV(System.lineSeparator())
        logV("final modules to generate router")
        logV(finalModuleList.joinToString())

        collectRouter(finalModuleList)
        collectService(finalModuleList)

    }

    private fun getModuleNameFromFile(name: String): String {
        return try {
            name.filter { it in '0'..'9' || it in 'A'..'Z' || it in 'a'..'z' }.uppercase()
        } catch (e: Exception) {
            logV("failed to get module name: ${e.message}")
            ""
        }
    }


    private fun collectRouter(allModuleNameSet: Collection<String>) {
        val name = "RouterLoader"
        val routerFile =
            File(generatedDir.get().asFile, "io/john6/router/drouterlite/stub/$name.class")
        if (routerFile.exists()) {
            routerFile.delete()
        }
        routerFile.parentFile.mkdirs()
        routerFile.createNewFile()

        val byteArray = generateClass(allModuleNameSet.map { "${it}$name" }, name)
        FileOutputStream(routerFile, false).use {
            it.write(byteArray)
        }
    }

    private fun collectService(allModuleNameSet: Collection<String>) {
        val name = "ServiceLoader"
        val serviceFile =
            File(generatedDir.get().asFile, "io/john6/router/drouterlite/stub/${name}.class")
        if (serviceFile.exists()) {
            serviceFile.delete()
        }
        serviceFile.parentFile.mkdirs()
        serviceFile.createNewFile()

        val byteArray = generateClass(allModuleNameSet.map { "${it}${name}" }, name)
        FileOutputStream(serviceFile, false).use {
            it.write(byteArray)
        }
    }


    private fun generateClass(clazzList: List<String>, name: String): ByteArray {
        val packageName = "io/john6/router/drouterlite/stub"
        val cw = ClassWriter(0)
        // 定义一个 RouterLoader 类
        cw.visit(
            Opcodes.V1_8,
            Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL,
            "$packageName/$name",
            null,
            "java/lang/Object",
            null
        )
        // 生成默认的构造方法
        val mw = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null)
        // 生成构造方法的字节码指令
        mw.visitVarInsn(Opcodes.ALOAD, 0)
        mw.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        mw.visitInsn(Opcodes.RETURN)
        mw.visitMaxs(1, 1)
        mw.visitEnd()

        val signature = if (name == "RouterLoader") {
            "(Ljava/util/Map<Ljava/lang/String;+Ljava/lang/Object;>;)V"
        } else {
            "(Ljava/util/Map<Ljava/lang/Class<*>;Ljava/lang/Class<*>;>;)V"
        }

        // 生成 loadAll 方法
        val mv = cw.visitMethod(
            Opcodes.ACC_PUBLIC or Opcodes.ACC_FINAL,
            "loadAll",
            "(Ljava/util/Map;)V",
            signature,
            null
        )
        clazzList.forEach {
            val owner = "${packageName}/$it"
            mv.visitFieldInsn(Opcodes.GETSTATIC, owner, "INSTANCE", "L$owner;")
            mv.visitVarInsn(Opcodes.ALOAD, 1)
            mv.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                owner,
                "loadAll",
                "(Ljava/util/Map;)V",
                false
            )

        }
        mv.visitInsn(Opcodes.RETURN)
        mv.visitMaxs(2, 2)

        // 字节码生成完毕
        mv.visitEnd()

        return cw.toByteArray()
    }
}