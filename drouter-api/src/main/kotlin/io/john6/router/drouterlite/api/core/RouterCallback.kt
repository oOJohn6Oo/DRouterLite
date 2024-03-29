package io.john6.router.drouterlite.api.core

import android.content.Intent

/**
 * Created by gaowei on 2018/9/5
 */
interface RouterCallback {
    fun onResult(result: RouterResult)

    /**
     * ActivityResult for [android.app.Activity.startActivityForResult] request.
     * You can also assign request code, [com.didi.drouter.api.Extend.START_ACTIVITY_REQUEST_CODE]
     *
     */
    @Deprecated(
        """use
      {@link Request#setActivityResultLauncher(ActivityResultLauncher)}"""
    )
    abstract class ActivityCallback : RouterCallback {
        override fun onResult(result: RouterResult) {}
        abstract fun onActivityResult(resultCode: Int, data: Intent?)
    }
}
