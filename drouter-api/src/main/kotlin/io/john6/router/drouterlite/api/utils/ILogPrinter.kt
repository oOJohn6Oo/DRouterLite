package io.john6.router.drouterlite.api.utils

import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY)
interface ILogPrinter {
    fun d(tag: String, content: String)
    fun w(tag: String, content: String)
    fun e(tag: String, content: String)
}