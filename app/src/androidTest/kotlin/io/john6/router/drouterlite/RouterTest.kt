package io.john6.router.drouterlite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.containsString
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RouterTest {

    @get:Rule
    var mainActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun just_jump_test(){
        Intents.init()
        onView(withId(R.id.check_box_nav_with_callback)).check(matches(isNotChecked()))
        onView(withId(R.id.check_box_nav_using_launcher)).check(matches(isNotChecked()))
        onView(withId(R.id.btn_next)).perform(click())
        val intent = Intents.getIntents()[0]
        Intents.release()
        router_destination_is_right_test(intent)
        router_with_extra_is_intact_test(intent)
    }

    @Test
    fun jump_with_callback_test(){
        Intents.init()
        onView(withId(R.id.check_box_nav_with_callback)).check(matches(isNotChecked()))
            .perform(click())
        onView(withId(R.id.check_box_nav_using_launcher)).check(matches(isNotChecked()))
        onView(withId(R.id.btn_next)).perform(click())
        val intent = Intents.getIntents()[0]
        Intents.release()
        router_destination_is_right_test(intent)
        router_callback_result_test(intent)
    }

    @Test
    fun jump_using_launcher_test(){
        Intents.init()
        onView(withId(R.id.check_box_nav_with_callback)).check(matches(isNotChecked()))
        onView(withId(R.id.check_box_nav_using_launcher)).check(matches(isNotChecked()))
            .perform(click())
        onView(withId(R.id.btn_next)).perform(click())
        val intent = Intents.getIntents()[0]
        Intents.release()
        router_destination_is_right_test(intent)
        router_with_extra_is_intact_test(intent)

        Thread.sleep(1000L)
        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack())
        onView(withId(R.id.content)).check(matches(withText(containsString(Activity.RESULT_CANCELED.toString()))))
    }

    @Test(timeout = 12000L)
    fun jump_using_launcher_with_callback_test(){
        Intents.init()
        onView(withId(R.id.check_box_nav_with_callback)).check(matches(isNotChecked()))
            .perform(click())
        onView(withId(R.id.check_box_nav_using_launcher)).check(matches(isNotChecked()))
            .perform(click())
        onView(withId(R.id.btn_next)).perform(click())
        val intent = Intents.getIntents()[0]
        Intents.release()
        router_destination_is_right_test(intent)
        router_callback_result_test(intent)
        // press back to finish the activity
        Thread.sleep(1000L)
        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack())
        onView(withId(R.id.content)).check(matches(withText(containsString(Activity.RESULT_CANCELED.toString()))))
    }

    @Test(timeout = 2000L)
    fun show_dialog_fragment_test(){
        onView(withId(R.id.btn_fragment)).perform(click())
        Thread.sleep(1000L)
        onView(withText(containsString("Hello DRouterLite")))
            .check(matches(isDisplayed()))
    }

    private fun router_callback_result_test(intent: Intent) {
        val extras = intent.extras
        Assert.assertNotNull(extras)
        val content = extras?.getString("content")
        Assert.assertEquals(MainActivity.CONTENT_CALLBACK_TEST, content)

        // check toast shows
        onView(withId(R.id.content)).check(matches(withText(containsString(MainActivity.CONTENT_CALLBACK_TEST))))
    }

    private fun router_destination_is_right_test(intent: Intent){
        Assert.assertEquals("wrong destination", intent.component?.className, MainActivity::class.java.name)
    }

    private fun router_with_extra_is_intact_test(intent: Intent) {
        val extras = intent.extras
        Assert.assertNotNull(extras)
        val content = extras?.getString("content")

        val desireExtras = Bundle().apply {
            putAll(MainActivity.test_bundle)
            putString("content", content)
        }

        Assert.assertEquals("extra is broken", desireExtras.toString(), extras.toString())
    }
}