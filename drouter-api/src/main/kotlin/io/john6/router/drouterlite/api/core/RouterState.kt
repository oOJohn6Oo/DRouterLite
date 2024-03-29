package io.john6.router.drouterlite.api.core

import androidx.annotation.IntDef

/**
 * Created by gaowei on 2019/1/27
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(RouterState.OK, RouterState.NOT_FOUND)
annotation class RouterState {
    companion object {
        const val OK = 0
        const val NOT_FOUND = 1
    }
}
