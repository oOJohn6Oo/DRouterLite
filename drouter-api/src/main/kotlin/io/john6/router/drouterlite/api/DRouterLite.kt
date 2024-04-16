package io.john6.router.drouterlite.api

import android.app.Application
import android.content.pm.ApplicationInfo
import io.john6.router.drouterlite.stub.RouterLoader
import io.john6.router.drouterlite.api.core.RouterMeta
import io.john6.router.drouterlite.api.core.RouterRequest
import io.john6.router.drouterlite.api.core.ServiceMeta
import io.john6.router.drouterlite.api.utils.DRouterLiteLogger
import io.john6.router.drouterlite.api.utils.DRouterLiteLogger.CORE_TAG
import io.john6.router.drouterlite.stub.ServiceLoader
import java.lang.ref.SoftReference

class DRouterLite internal constructor(
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

    internal fun getRouterMetaByPath(path:String): RouterMeta? {
        val res = routerMap[path]
        DRouterLiteLogger.d(CORE_TAG, "DRouterLite $path get router: $res")
        return res
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <T> getServiceClassByClass(clazz: Class<T>): T? {
        val cacheInstance = serviceInstanceCacheMap[clazz]?.get()
        if(cacheInstance != null){
            DRouterLiteLogger.d(CORE_TAG, "DRouterLite $clazz get cache service: ${cacheInstance::class.java}")
            return cacheInstance as? T
        }
        val newInstance = serviceMap[clazz]?.newInstance() as? T
        if(newInstance != null){
            serviceInstanceCacheMap[clazz] = SoftReference(newInstance)
        }
        DRouterLiteLogger.d(CORE_TAG, "DRouterLite $clazz get new service: ${newInstance?.let { it::class.java }}")
        return newInstance
    }

    companion object{

        internal lateinit var instance: DRouterLite

        fun <T> build(clazz: Class<T>): T? {
            return instance.getServiceClassByClass(clazz)
        }

        fun build(path: String): RouterRequest {
            return RouterRequest.build(path)
        }
    }
}