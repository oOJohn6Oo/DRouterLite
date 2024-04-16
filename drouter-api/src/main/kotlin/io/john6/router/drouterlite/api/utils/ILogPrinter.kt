package io.john6.router.drouterlite.api.utils

internal interface ILogPrinter {
    fun d(tag: String, content: String)
    fun w(tag: String, content: String)
    fun e(tag: String, content: String)
}