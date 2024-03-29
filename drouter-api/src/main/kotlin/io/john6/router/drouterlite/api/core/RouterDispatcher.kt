package io.john6.router.drouterlite.api.core

import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import io.john6.router.drouterlite.api.utils.Extend
import io.john6.router.drouterlite.api.core.RouterCallback.ActivityCallback
import io.john6.router.drouterlite.api.utils.DRouterLiteLogger
import io.john6.router.drouterlite.api.utils.DRouterLiteLogger.CORE_TAG

/**
 * Created by gaowei on 2018/9/5
 */
internal object RouterDispatcher {
    fun start(
        routerRequest: RouterRequest,
        meta: RouterMeta,
        callback: RouterCallback?
    ) {
        DRouterLiteLogger.d(
            CORE_TAG,
            "request \"%s\", class \"%s\" start execute",
            routerRequest.number,
            meta.clazz.simpleName
        )
        when (meta.routerType) {
            RouterType.ACTIVITY -> startActivity(routerRequest, meta, callback)
            RouterType.FRAGMENT -> startFragment(routerRequest, meta, callback)
            else -> {}
        }
    }

    private fun startActivity(
        routerRequest: RouterRequest,
        meta: RouterMeta,
        callback: RouterCallback?
    ) {
        val context = routerRequest.context
        val intent = Intent().apply {
            setClass(context, meta.clazz)
            putExtras(routerRequest.bundle)
            if (routerRequest.bundle.containsKey(Extend.START_ACTIVITY_FLAGS)) {
                setFlags(routerRequest.bundle.getInt(Extend.START_ACTIVITY_FLAGS))
            }
            if (context !is Activity) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
        val hasRequestCode = routerRequest.bundle.containsKey(Extend.START_ACTIVITY_REQUEST_CODE)
        val requestCode =
            if (hasRequestCode) routerRequest.bundle.getInt(Extend.START_ACTIVITY_REQUEST_CODE) else 1024

        val launcher = routerRequest.activityResultLauncher
        if (launcher != null) {
            launcher.launch(intent)
        } else if (context is Activity && callback is ActivityCallback) {
            ActivityCompat2.startActivityForResult(context, intent, requestCode, callback)
        } else if (context is Activity && hasRequestCode) {
            ActivityCompat.startActivityForResult(
                context, intent,
                requestCode, intent.getBundleExtra(Extend.START_ACTIVITY_OPTIONS)
            )
        } else {
            ActivityCompat.startActivity(
                context,
                intent,
                intent.getBundleExtra(Extend.START_ACTIVITY_OPTIONS)
            )
        }
        val anim = routerRequest.bundle.getIntArray(Extend.START_ACTIVITY_ANIMATION)
        if (context is Activity && anim != null && anim.size == 2) {
            context.overridePendingTransition(anim[0], anim[1])
        }
        callback?.onResult(RouterResult(RouterResult.SUCCESS, meta.clazz))
    }

    private fun startFragment(
        routerRequest: RouterRequest,
        meta: RouterMeta,
        callback: RouterCallback?
    ) {

        var desireFragment: Fragment? = null

        if (routerRequest.bundle.getBoolean(Extend.START_FRAGMENT_NEW_INSTANCE, true)) {
            desireFragment = meta.clazz.getDeclaredConstructor().newInstance() as? Fragment
            desireFragment?.setArguments(routerRequest.bundle)
        }
        callback?.onResult(RouterResult(RouterResult.SUCCESS, meta.clazz, desireFragment))
    }

    fun onRouterNotFound(callback: RouterCallback?) {
        callback?.onResult(RouterResult())
    }
}
