@file:Suppress("unused")

package io.john6.router.drouterlite.api.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import io.john6.router.drouterlite.api.DRouterLite

class RouterRequest private constructor(var path: String){
    lateinit var context: Context
    private set
    var activityResultLauncher: ActivityResultLauncher<Intent>? = null
    private set
    val bundle = Bundle()

    inline fun withExtras(action: (Bundle.() -> Unit)): RouterRequest {
        bundle.apply {
            action()
        }
        return this
    }

    @VisibleForTesting
    fun replaceBundle(bundle: Bundle): RouterRequest {
        this.bundle.clear()
        this.bundle.putAll(bundle)
        return this
    }

    /**
     * @param context If you want to return ActivityResult,
     * please use Activity for context and ActivityCallback for RouterCallback.
     * @param launcher when launcher is set, context must be Activity，
     * or onActivityResult will immediate called with code [android.app.Activity.RESULT_CANCELED]
     * @param callback Result to return.
     */
    fun start(context: Context? = null,
              launcher: ActivityResultLauncher<Intent>? = null,
              callback: RouterCallback? = null,
    ) {
        if(!DRouterLite.isAlreadyInit()) {
            RouterDispatcher.onDRouteNotInit(callback)
            return
        }
        this.context = context ?: DRouterLite.instance.mApp
        val routerMeta = DRouterLite.instance.getRouterMetaByPath(path)
        if (routerMeta == null) {
            RouterDispatcher.onRouterNotFound(callback)
            return
        }
        activityResultLauncher = launcher
        // single result for multi request
        RouterDispatcher.start(this, routerMeta, callback)
    }

    fun start(context: Context? = null, callback: RouterCallback? = null) {
        start(context, null, callback)
    }

    fun start(){
        start(null, null, null)
    }

    fun getFragment(callback: RouterCallback? = null): Fragment? {
        if(!DRouterLite.isAlreadyInit()) {
            RouterDispatcher.onDRouteNotInit(callback)
            return null
        }
        val routerMeta = DRouterLite.instance.getRouterMetaByPath(path)
        if (routerMeta == null) {
            RouterDispatcher.onRouterNotFound(callback)
            return null
        }

        if(!Fragment::class.java.isAssignableFrom(routerMeta.clazz)){
            callback?.onResult(RouterResult(RouterResult.WRONG_TYPE, routerMeta.clazz))
            return null
        }
        return RouterDispatcher.getFragment(this, routerMeta, callback)
    }

    companion object {
        fun build(path: String): RouterRequest {
            return RouterRequest(path)
        }
    }
}
