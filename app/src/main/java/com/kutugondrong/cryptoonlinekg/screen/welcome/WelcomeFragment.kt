package com.kutugondrong.cryptoonlinekg.screen.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kutugondrong.cryptoonlinekg.R
import com.kutugondrong.cryptoonlinekg.preference.LocalPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.welcome_fragment) {

    @Inject lateinit var localPreferences: LocalPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).actionBar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(1000L)
            if (localPreferences.isLogin) {
                findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
            } else {
                findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
            }
        }
    }

}