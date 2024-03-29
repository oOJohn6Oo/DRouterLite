package io.john6.router.drouterlite.api

import android.app.Application
import android.content.pm.ApplicationInfo
import androidx.annotation.RestrictTo
import io.john6.router.drouterlite.stub.RouterLoader
import io.john6.router.drouterlite.api.core.RouterMeta
import io.john6.router.drouterlite.api.core.RouterRequest
import io.john6.router.drouterlite.api.core.ServiceMeta
import io.john6.router.drouterlite.api.utils.DRouterLiteLogger
import io.john6.router.drouterlite.api.utils.DRouterLiteLogger.CORE_TAG
import io.john6.router.drouterlite.stub.ServiceLoader
import java.lang.ref.SoftReference

@RestrictTo(RestrictTo.Scope.LIBRARY)
class DRouterLite(
    val mApp: Application,
) {
    val isDebug: Boolean = isDebuggable()
    private val routerMap = hashMapOf<String, RouterMeta>()
    private val serviceMap = hashMapOf<Class<*>, ServiceMeta<Any>>()
    private val serviceInstanceCacheMap = hashMapOf<Class<*>, SoftReference<Any>>()

    init {
        loadAll()
    }

    private fun loadAll() {
        RouterLoader().loadAll(routerMap)
        ServiceLoader().loadAll(serviceMap)
    }

    private fun isDebuggable(): Boolean {
        var res = false
        try {
            val info = mApp.applicationInfo
            res = info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (ignore: Exception) {
        }
        DRouterLiteLogger.d(CORE_TAG, "drouterlite is debug: $res")
        return res
    }

    fun getRouterMetaByPath(path:String): RouterMeta? {
        return routerMap[path]
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getServiceClassByClass(clazz: Class<T>): T? {
        val cacheInstance = serviceInstanceCacheMap[clazz]?.get()
        if(cacheInstance != null){
            return cacheInstance as? T
        }
        val newInstance = serviceMap[clazz]?.newInstance() as? T
        if(newInstance != null){
            serviceInstanceCacheMap[clazz] = SoftReference(newInstance)
        }
        return newInstance
    }

    companion object{

        @RestrictTo(RestrictTo.Scope.LIBRARY)
        lateinit var instance: DRouterLite

        fun <T> build(clazz: Class<T>): T? {
            return instance.getServiceClassByClass(clazz)
        }

        fun build(path: String): RouterRequest {
            return RouterRequest.build(path)
        }
    }
}