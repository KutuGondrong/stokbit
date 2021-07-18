package com.kutugondrong.cryptoonlinekg.screen

import android.support.test.espresso.IdlingRegistry
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.kutugondrong.cryptoonlinekg.MockServerDispatcher
import com.kutugondrong.cryptoonlinekg.R
import com.kutugondrong.cryptoonlinekg.helper.waitViewShown
import com.kutugondrong.cryptoonlinekg.launchFragmentInHiltContainer
import com.kutugondrong.cryptoonlinekg.screen.home.HomeFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@UninstallModules(AppModule::class)
@HiltAndroidTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: CryptoOnlineFragmentFactoryAndroidTest

    private lateinit var mockWebServer: MockWebServer

    @Inject lateinit var okHttp: OkHttpClient

    @Before
    fun init() {
        hiltRule.inject()
        mockWebServer = MockWebServer()
        mockWebServer.dispatcher = MockServerDispatcher().RequestDispatcher()
        mockWebServer.start(8080)
        IdlingRegistry.getInstance().register(OkHttp3IdlingResource.create("okhttp", okHttp))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testVisibilityHomeFragment(){
        launchFragmentInHiltContainer<HomeFragment>(fragmentFactory = fragmentFactory)
        waitViewShown(withId(R.id.progressHome), false)
    }

    @Test
    fun testScrollListHome(){
        launchFragmentInHiltContainer<HomeFragment>(fragmentFactory = fragmentFactory)
        waitViewShown(withId(R.id.progressHome), false)
        for (i in 1..10) {
            onView(withId(R.id.recycleCrypto)).perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(i*3, ViewActions.scrollTo())
            )
        }
    }


}



