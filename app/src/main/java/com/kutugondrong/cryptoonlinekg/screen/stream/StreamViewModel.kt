package com.kutugondrong.cryptoonlinekg.screen.stream

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutugondrong.cryptoonlinekg.data.repository.SocketRepository
import com.kutugondrong.cryptoonlinekg.data.socket.model.TickerResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StreamViewModel @Inject constructor(
    private val repository: SocketRepository
) : ViewModel() {


    fun subscribe(update: (TickerResponse) -> Unit) {
        viewModelScope.launch {
            repository.observeCryptoSocket{ data: TickerResponse -> update(data) }
        }
        repository.subscribe()
    }

}