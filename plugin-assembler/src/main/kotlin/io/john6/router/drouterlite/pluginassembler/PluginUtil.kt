package io.john6.router.drouterlite.pluginassembler

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ResolvedDependency

internal fun Dependency.matchLocalCollector(): Boolean {
    return group?.startsWith("DRouterLite") ?: false && name == "plugin-collector" && version == "unspecified"
}

internal fun Dependency.matchRemoteCollector(): Boolean {
    return group == "io.github.oojohn6oo" && name == "drouterlite-collector"
}

internal fun ResolvedDependency.matchLocalCollector(): Boolean {
    return moduleGroup.startsWith("DRouterLite") && moduleName == "plugin-collector" && moduleVersion == "unspecified"
}

internal fun ResolvedDependency.matchRemoteCollector(): Boolean {
    return moduleGroup == "io.github.oojohn6oo" && moduleName == "drouterlite-collector"
}

internal fun Dependency.isLocalModule(): Boolean {
    return version == "unspecified"
}

internal fun ResolvedDependency.isLocalModule(): Boolean {
    return moduleVersion == "unspecified"
}

fun getAllInvolvedModuleName(
    dep: Collection<ResolvedDependency>,
    allModuleSet: MutableSet<String>,
    rootProjectName:String
) {
    dep.filter { it.moduleGroup.startsWith(rootProjectName) }.forEach {
        allModuleSet.add(it.moduleName)
        val desireChildDep = it.children.filter {childDep-> childDep.isLocalModule() }
        getAllInvolvedModuleName(desireChildDep, allModuleSet, rootProjectName)
    }
}

fun getAllCollectorModuleName(
    dep: Collection<ResolvedDependency>,
    finalResSet: MutableSet<String>,
    rootProjectName:String
) {
    dep.filter { true || it.moduleGroup.startsWith(rootProjectName) }.forEach {
        logV(it)
        it.children.forEach { childDep->
            if(childDep.matchLocalCollector() || childDep.matchRemoteCollector() ){
                finalResSet.add(it.moduleName)
            }
            if(childDep.isLocalModule()){
                getAllCollectorModuleName(childDep.children, finalResSet, rootProjectName)
            }
        }
    }
}



internal fun logV(msg: Any) {
    println("\u001b[36m$msg\u001b[0m")
}