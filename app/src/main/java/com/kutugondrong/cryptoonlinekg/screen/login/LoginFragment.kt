package com.kutugondrong.cryptoonlinekg.screen.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kutugondrong.cryptoonlinekg.R
import com.kutugondrong.cryptoonlinekg.databinding.LoginFragmentBinding
import com.kutugondrong.cryptoonlinekg.preference.LocalPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment) {

    val viewModel by viewModels<LoginViewModel>()
    @Inject lateinit var localPreferences: LocalPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = LoginFragmentBinding.bind(view)
        binding.apply {
            btnLogin.setOnClickListener{
                localPreferences.isLogin = true
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
    }

}