package io.john6.router.drouterlite.api.core

import androidx.annotation.RestrictTo

/**
 * Created by gaowei on 2018/8/30
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
data class RouterMeta(
    val path:String,
    @RouterType val routerType: Int,
    val clazz:Class<*>
)