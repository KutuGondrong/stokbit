package com.kutugondrong.cryptoonlinekg.screen.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kutugondrong.cryptoonlinekg.MainActivity
import com.kutugondrong.cryptoonlinekg.R
import com.kutugondrong.cryptoonlinekg.databinding.HomeFragmentBinding
import com.kutugondrong.cryptoonlinekg.utils.RecyclerViewItemDecoration
import com.kutugondrong.data.local.entity.Crypto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {

    private var cryptoJob: Job? = null

    private val viewModel: HomeViewModel by viewModels()

    private val adapter =
        CryptoItemAdapter { data: Crypto -> snackBarClickedData(data) }

    private fun snackBarClickedData(data: Crypto) {
        Snackbar.make(requireView().rootView, data.fullName, Snackbar.LENGTH_LONG)
            .show()
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = HomeFragmentBinding.bind(view)
        binding.apply {
            (activity as? MainActivity)?.showToolbar(true)
            (activity as? MainActivity)?.showNavBottom(View.VISIBLE)
            recycleCrypto.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                addItemDecoration(RecyclerViewItemDecoration())
            }
            recycleCrypto.adapter = adapter.withLoadStateFooter(
                footer = CryptoLoadingStateAdapter { retry() }
            )

            adapter.addLoadStateListener { loadState ->

                if (loadState.mediator?.refresh is LoadState.Loading) {

                    if (adapter.snapshot().isEmpty()) {
                        progressHome.isVisible = true
                    }
                    errorTxt.isVisible = false
                } else {
                    progressHome.isVisible = false
                    swipeRefreshLayout.isRefreshing = false
                    val error = when {
                        loadState.mediator?.prepend is LoadState.Error -> loadState.mediator?.prepend as LoadState.Error
                        loadState.mediator?.append is LoadState.Error -> loadState.mediator?.append as LoadState.Error
                        loadState.mediator?.refresh is LoadState.Error -> loadState.mediator?.refresh as LoadState.Error

                        else -> null
                    }
                    error?.let {
                        if (adapter.snapshot().isEmpty()) {
                            errorTxt.isVisible = true
                            errorTxt.text = it.error.localizedMessage
                        }

                    }

                }
            }

            startCryptoJob()

            swipeRefreshLayout.setOnRefreshListener {
                adapter.refresh()
            }

        }

    }

    private fun retry() {
        adapter.retry()
    }

    @ExperimentalPagingApi
    private fun startCryptoJob() {
        cryptoJob?.cancel()
        cryptoJob = lifecycleScope.launch {
            viewModel.getCryptos()
                .collectLatest {
                    adapter.submitData(it)
                }
        }

    }

}
