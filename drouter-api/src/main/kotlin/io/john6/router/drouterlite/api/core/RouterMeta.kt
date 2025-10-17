package io.john6.router.drouterlite.api.core

/**
 * Created by gaowei on 2018/8/30
 */
data class RouterMeta(
    val path:String,
    @param:RouterType val routerType: Int,
    val clazz:Class<*>
)