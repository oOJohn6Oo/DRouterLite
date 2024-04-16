package io.john6.router.drouterlite.api.utils

import android.util.Log
import io.john6.router.drouterlite.api.DRouterLite

internal object DRouterLiteLogger{
    const val CORE_TAG = "DRouterCore"
    private val printer = InnerLogPrinter()

    private fun enablePrint(): Boolean {
        return DRouterLite.instance.isDebug
    }


    fun d(tag: String, content: String, vararg args: Any?) {
        printer.d(tag, format(content, *args))
    }

    fun w(tag: String, content: String, vararg args: Any?) {
        printer.w(tag, format(content, *args))
    }

    fun e(tag: String, content: String, vararg args: Any?) {
        printer.e(tag, format(content, *args))
    }

    fun crash(tag:String, content: String, vararg args: Any?) {
        if (enablePrint()) {
            printer.e(
                tag, """${format(content, *args)}
 Exception:${Log.getStackTraceString(Throwable())}"""
            )
        }
        throw RuntimeException(content)
    }

    private fun format(s: String, vararg args: Any?): String {
        val desireArgs = args.map {
            if (it is Throwable) {
                Log.getStackTraceString(it)
            } else it
        }.toTypedArray()
        return String.format(s, *desireArgs)
    }

}