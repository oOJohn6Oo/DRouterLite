package io.john6.router.drouterlite.plugincollector

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import java.io.OutputStream

class DRouterLiteRouterVisitor(
    private val file: OutputStream?,
    private val path: String,
    private val typeAtt: KSType,
    private val map: HashMap<String, String>
) : KSVisitorVoid() {

    private fun emit(s: String, indent: String = "") {
        file?.appendText("$indent$s\n")
    }

    private val visited = HashSet<Any>()

    private fun checkVisited(symbol: Any): Boolean {
        return if (visited.contains(symbol)) {
            true
        } else {
            visited.add(symbol)
            false
        }
    }

    override fun visitAnnotation(annotation: KSAnnotation, data: Unit) {
        if (checkVisited(annotation)) return
        annotation.annotationType.accept(this, data)
        annotation.arguments.forEach { it.accept(this, data) }
    }

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        if (checkVisited(classDeclaration)) return
        val classType = classDeclaration.asType(listOf())
        if(typeAtt.isAssignableFrom(classType)){
            val className = classDeclaration.simpleName.getShortName()
            val classFullName = classDeclaration.packageName.asString() + "." + className
            map[path] = classFullName
        }
    }

}