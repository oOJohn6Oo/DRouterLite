package io.john6.router.drouterlite.api.core

import androidx.fragment.app.Fragment

data class RouterResult(
    var statusCode: Int = NOT_FOUND,
    var routerClass: Class<*>? = null,
    var fragment: Fragment? = null
) {

    companion object {
        // status code example
        // Users can customize their own status code
        const val SUCCESS: Int = 200
        const val NOT_FOUND: Int = 404
    }
}
