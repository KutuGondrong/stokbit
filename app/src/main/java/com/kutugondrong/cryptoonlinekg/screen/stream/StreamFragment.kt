package com.kutugondrong.cryptoonlinekg.screen.stream

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.kutugondrong.cryptoonlinekg.R
import com.kutugondrong.cryptoonlinekg.databinding.StreamFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StreamFragment : Fragment(R.layout.stream_fragment) {

    private val viewModel: StreamViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = StreamFragmentBinding.bind(view)

        binding.apply {
            txtName.text = "-----"
            txtSymbol.text = "-----"
            txtPrice.text = "------"
            viewModel.dataCrypto.observe(viewLifecycleOwner) {
                it.fromSymbol?.apply {
                    txtName.text = this
                }
                it.toSymbol?.apply {
                    txtSymbol.text = this
                }
                it.price?.apply {
                    txtPrice.text = this
                }
            }

            viewModel.subscribeFirst()

            cardStream.setOnClickListener{
                viewModel.subscribe()
            }

        }



    }

}