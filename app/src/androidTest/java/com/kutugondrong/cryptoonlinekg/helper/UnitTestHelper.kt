package com.kutugondrong.cryptoonlinekg.helper

import android.view.View
import androidx.core.view.isGone
import androidx.test.espresso.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.tabs.TabLayout
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import java.lang.reflect.Field

fun selectTabAtPosition(tabIndex: Int): ViewAction {
    return object : ViewAction {
        override fun getDescription() = "with tab at index $tabIndex"

        override fun getConstraints() = CoreMatchers.allOf(
            ViewMatchers.isDisplayed(),
            ViewMatchers.isAssignableFrom(TabLayout::class.java)
        )

        override fun perform(uiController: UiController, view: View) {
            val tabLayout = view as TabLayout
            val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                ?: throw PerformException.Builder()
                    .withCause(Throwable("No tab at index $tabIndex"))
                    .build()

            tabAtIndex.select()
        }
    }
}

class ViewShownIdlingResource(private val viewMatcher: Matcher<View?>?, private val isAccessible: Boolean = true) :
    IdlingResource {
    private var resourceCallback: IdlingResource.ResourceCallback? = null
    override fun isIdleNow(): Boolean {
        val view = getView(viewMatcher)
        val idle =  view == null || (if (isAccessible) view.isShown else view.isGone )
        if (idle && resourceCallback != null) {
            resourceCallback!!.onTransitionToIdle()
        } else if (!idle && resourceCallback != null) {
            resourceCallback!!.onTransitionToIdle()
        }
        return idle
    }

    override fun registerIdleTransitionCallback(resourceCallback: IdlingResource.ResourceCallback) {
        this.resourceCallback = resourceCallback
    }

    override fun getName(): String {
        return this.toString() + viewMatcher.toString()
    }

    companion object {
        private fun getView(viewMatcher: Matcher<View?>?): View? {
            return try {
                val viewInteraction = Espresso.onView(viewMatcher)
                val finderField: Field = viewInteraction.javaClass.getDeclaredField("viewFinder")
                finderField.isAccessible = true
                val finder = finderField.get(viewInteraction) as ViewFinder
                finder.view
            } catch (e: Exception) {
                null
            }
        }
    }
}

inline fun waitViewShown(matcher: Matcher<View?>?, isAccessible: Boolean = true) {
    val idlingResource: IdlingResource = ViewShownIdlingResource(matcher, isAccessible) ///
    try {
        IdlingRegistry.getInstance().register(idlingResource)
        if (isAccessible) {
            Espresso.onView(matcher).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        } else {
            Espresso.onView(matcher).check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
        }
    } finally {
        IdlingRegistry.getInstance().unregister(idlingResource)
    }
}