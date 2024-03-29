package io.john6.router.drouterlite.annotation

import kotlin.reflect.KClass


/**
 * Created by gaowei on 2018/8/30
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Service(
    val clazz: KClass<*>
)
