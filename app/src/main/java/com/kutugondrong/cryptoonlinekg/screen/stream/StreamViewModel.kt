package com.kutugondrong.cryptoonlinekg.screen.stream

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutugondrong.cryptoonlinekg.data.repository.SocketRepository
import com.kutugondrong.data.socket.model.TickerResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StreamViewModel @Inject constructor(
    private val repository: SocketRepository
) : ViewModel() {

    private val dataInput = MutableLiveData<TickerResponse>()
    val dataCrypto: LiveData<TickerResponse> = dataInput

    init {
        viewModelScope.launch {
            repository.observeCryptoSocket{
                dataInput.value = it
            }
        }
    }
    fun subscribe() {
        viewModelScope.launch {
            repository.subscribe(listOf("2~Coinbase~BTC~USD"))
        }
    }

}