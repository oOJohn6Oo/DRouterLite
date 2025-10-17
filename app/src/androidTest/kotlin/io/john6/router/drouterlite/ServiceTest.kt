package io.john6.router.drouterlite

import android.app.Activity
import android.os.Build
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.containsString
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ServiceTest {

    @get:Rule
    var mainActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_direct_service_call(){
        // FIXME This test is not working on Android 11
        // https://github.com/android/android-test/issues/803
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            return
        }
        onView(withId(R.id.btn_service)).perform(click())
        val content = MainActivity.CONTENT_SERVICE_MESSAGE
        Assert.assertNotNull(content)
        Thread.sleep(1000L)
        onView(withId(R.id.content)).check(matches(withText(containsString(content))))
    }
}