package io.john6.router.drouterlite.api.startup

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import io.john6.router.drouterlite.api.DRouterLite

@Suppress("unused")
class DRouterLiteInitializer: Initializer<Unit> {

    override fun create(context: Context) {
        DRouterLite.instance = DRouterLite(context.applicationContext as Application)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}