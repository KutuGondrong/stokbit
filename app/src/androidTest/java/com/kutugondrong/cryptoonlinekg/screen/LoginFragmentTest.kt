package com.kutugondrong.cryptoonlinekg.screen

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kutugondrong.cryptoonlinekg.screen.login.LoginFragment
import com.kutugondrong.cryptoonlinekg.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@UninstallModules(AppModule::class)
@HiltAndroidTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class LoginFragmentTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: CryptoOnlineFragmentFactoryAndroidTest

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testVisibilityLoginFragment(){
        launchFragmentInHiltContainer<LoginFragment>(fragmentFactory = fragmentFactory)
    }


}


