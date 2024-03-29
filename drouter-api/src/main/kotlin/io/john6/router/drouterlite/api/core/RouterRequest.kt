package io.john6.router.drouterlite.api.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.VisibleForTesting
import io.john6.router.drouterlite.api.DRouterLite
import io.john6.router.drouterlite.api.utils.Extend
import java.util.concurrent.atomic.AtomicInteger

class RouterRequest private constructor(var path: String){
    lateinit var context: Context
    var number: String = counter.getAndIncrement().toString()
    var activityResultLauncher: ActivityResultLauncher<Intent>? = null

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

    companion object {
        private val counter = AtomicInteger(0)
        fun build(path: String): RouterRequest {
            return RouterRequest(path)
        }
    }
}
