package io.john6.router.drouterlite

import android.util.Log
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class ToastMatcher: TypeSafeMatcher<Root?>() {
    override fun describeTo(description: Description?) {
        description?.appendText("is toast")
    }

    override fun matchesSafely(item: Root?): Boolean {
        val type = item?.windowLayoutParams?.get()?.type ?: return false
        if(type == android.view.WindowManager.LayoutParams.TYPE_TOAST) {
            val windowToken = item.decorView.windowToken
            val appToken = item.decorView.applicationWindowToken
            if (windowToken === appToken) {
                return true
            }
        }
        return false
    }
}