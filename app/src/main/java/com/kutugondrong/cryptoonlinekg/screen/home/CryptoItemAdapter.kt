package com.kutugondrong.cryptoonlinekg.screen.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kutugondrong.cryptoonlinekg.R
import com.kutugondrong.cryptoonlinekg.data.local.entity.Crypto
import com.kutugondrong.cryptoonlinekg.databinding.AdapterItemCryptoBinding

class CryptoItemAdapter(
    private val clicked: (Crypto) -> Unit
) :
    PagingDataAdapter<Crypto, CryptoItemAdapter.ViewHolder>(COMPARATOR) {

    inner class ViewHolder(private val binding: AdapterItemCryptoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Crypto) {
            with(binding) {
                Glide.with(itemView)
                    .load(data.fullUrlImage)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_launcher_foreground)
                    )
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imgIcon)
                txtName.text = data.fullName
                txtSymbol.text = data.name
                txtPrice.text = data.price
                itemView.setOnClickListener{
                    clicked(data)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterItemCryptoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Crypto>() {
            override fun areItemsTheSame(oldItem: Crypto, newItem: Crypto): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Crypto, newItem: Crypto): Boolean =
                oldItem == newItem

        }
    }


}