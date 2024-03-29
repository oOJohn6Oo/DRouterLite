package io.john6.router.drouterlite.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Router(val path: String)
