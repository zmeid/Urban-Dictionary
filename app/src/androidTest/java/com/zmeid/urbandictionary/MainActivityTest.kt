package com.zmeid.urbandictionary

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.zmeid.urbandictionary.view.ui.MainActivity
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Contains tests for [MainActivity].
 */
@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    /**
     * Searches the letter 'b', plays the sound of the letter. Asserts that sound player is initialized and it is visible on the screen.
     */
    @Test
    fun mainActivityPlaySoundTest() {

        //Click Search
        onView(withId(R.id.menu_search)).perform(click())

        // Search for 'b'
        onView(withId(R.id.search_src_text)).perform(replaceText("b"), closeSoftKeyboard())

        // Sleep to wait API response
        Thread.sleep(3000)

        // Click Play Sound
        onView(withId(R.id.recyclerview_urban_results)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0, clickChildViewWithId(R.id.image_view_play_sound))
        )

        // Wait player to initialize
        Thread.sleep(1000)

        // Check If urbanPlayerControlView exists
        onView(withId(R.id.urbanPlayerControlView)).check(matches(isDisplayed()))
    }

    /**
     * Finds the child with given id and performs click.
     */
    private fun clickChildViewWithId(id: Int): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController?, view: View) {
                val v = view.findViewById<View>(id)
                v.performClick()
            }
        }
    }
}