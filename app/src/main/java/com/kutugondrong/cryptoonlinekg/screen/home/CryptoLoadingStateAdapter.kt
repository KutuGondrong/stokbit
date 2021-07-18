package com.kutugondrong.cryptoonlinekg.screen.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kutugondrong.cryptoonlinekg.databinding.AdapterLoadingStateItemCryptoBinding

class CryptoLoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<CryptoLoadingStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding =
            AdapterLoadingStateItemCryptoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: AdapterLoadingStateItemCryptoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                progressLoading.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState !is LoadState.Loading
                txtError.isVisible = loadState !is LoadState.Loading
            }
        }
    }
}