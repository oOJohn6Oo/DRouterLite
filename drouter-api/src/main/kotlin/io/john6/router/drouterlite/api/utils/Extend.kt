package io.john6.router.drouterlite.api.utils

/**
 * Created by gaowei on 2018/9/13
 */
object Extend {

    /**
     * value:Bundle, optionsBundle for Activity.
     */
    const val START_ACTIVITY_OPTIONS = "router_start_activity_options"

    /**
     * value:int[], animation for Activity.
     */
    const val START_ACTIVITY_ANIMATION = "router_start_activity_animation"

    /**
     * value:int, flags for Activity.
     */
    const val START_ACTIVITY_FLAGS = "router_start_activity_flags"

    /**
     * value:int, assign RequestCode for startActivityForResult.
     * [RouterCallback.ActivityCallback]
     */
    const val START_ACTIVITY_REQUEST_CODE = "router_start_activity_request_code"

    /**
     * value:Boolean，Used for Fragment, whether create fragment instance, default true.
     */
    const val START_FRAGMENT_NEW_INSTANCE = "router_start_fragment_new_instance"

    /**
     * value:Boolean，Used for View, whether create fragment instance, default true.
     */
    const val START_VIEW_NEW_INSTANCE = "router_start_view_new_instance"

}
