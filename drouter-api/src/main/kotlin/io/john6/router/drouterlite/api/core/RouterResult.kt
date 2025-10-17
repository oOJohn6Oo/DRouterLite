package io.john6.router.drouterlite.api.core

data class RouterResult(
    var statusCode: Int = NOT_FOUND,
    var routerClass: Class<*>? = null,
) {

    companion object {
        // status code example
        // Users can customize their own status code
        const val SUCCESS: Int = 200
        const val WRONG_TYPE: Int = 403
        const val NOT_FOUND: Int = 404
        const val NOT_INIT: Int = 500
    }
}
