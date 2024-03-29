package io.john6.router.drouterlite.api.core

/**
 * @param clazz the class of the service
 */
abstract class ServiceMeta<T>(
    val clazz:Class<*>,
){

    abstract fun newInstance(): T
}