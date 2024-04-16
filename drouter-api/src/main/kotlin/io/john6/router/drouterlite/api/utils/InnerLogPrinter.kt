package io.john6.router.drouterlite.api.utils

import android.util.Log

internal class InnerLogPrinter : ILogPrinter {

    override fun d(tag: String, content: String) {
        Log.d(tag, content)
    }

    override fun w(tag: String, content: String) {
        Log.w(tag, content)
    }

    override fun e(tag: String, content: String) {
        Log.e(tag, content)
    }
}