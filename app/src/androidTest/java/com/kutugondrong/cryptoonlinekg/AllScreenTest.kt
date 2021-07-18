package com.kutugondrong.cryptoonlinekg

import com.kutugondrong.cryptoonlinekg.screen.LoginFragmentTest
import com.kutugondrong.cryptoonlinekg.screen.HomeFragmentTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    LoginFragmentTest::class,
    HomeFragmentTest::class
)
class InstrumentationTestSuite