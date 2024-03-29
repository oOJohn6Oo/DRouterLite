package io.john6.router.drouterlite.api.core

/**
 * Created by gaowei on 2022/3/12
 *
 * GlobalListener to receive request result.
 */
interface IRouterResult {
    fun onResult(routerRequest: RouterRequest, StateCode: Int)
}

