package io.john6.router.drouterlite.pluginassembler


/**
 * 用于配置 RouterLoader 的配置
 *
 * [allModuleName] 与 [includeModuleName] 、 [excludeModuleName] 是互斥的
 */
open class DRouterLiteSetting(
    var includeModuleName: Set<String> = emptySet(),
    var excludeModuleName: Set<String> = emptySet(),
    var allModuleName: Set<String> = emptySet(),
)