package io.john6.router.drouterlite.api.core

import androidx.annotation.IntDef

/**
 * Created by gaowei on 2019/1/27
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(RouterType.ACTIVITY, RouterType.FRAGMENT)
annotation class RouterType {
    companion object {
        const val ACTIVITY = 1
        const val FRAGMENT = 2
    }
}
