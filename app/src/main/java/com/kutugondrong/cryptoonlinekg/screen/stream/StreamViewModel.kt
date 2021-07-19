package com.kutugondrong.cryptoonlinekg.screen.stream

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kutugondrong.cryptoonlinekg.data.repository.CryptosRepository
import com.kutugondrong.cryptoonlinekg.data.repository.SocketRepository
import com.kutugondrong.data.socket.model.TickerResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StreamViewModel @Inject constructor(
    private val repositorySocket: SocketRepository,
    private val repositoryCrypto: CryptosRepository
) : ViewModel() {

    private val dataInput = MutableLiveData<TickerResponse>()
    val dataCrypto: LiveData<TickerResponse> = dataInput

    private var cryptoJob: Job? = null

    init {
        viewModelScope.launch {
            repositorySocket.observeCryptoSocket{
                dataInput.value = it
            }
        }
    }
    fun subscribeFirst() {
        viewModelScope.launch {
            repositorySocket.subscribeFirst(listOf("2~Coinbase~BTC~USD"))
        }
    }

    fun subscribe() {
        cryptoJob?.cancel()
        cryptoJob = viewModelScope.launch {
            repositorySocket.subscribe(repositoryCrypto.getSymbolRandom())
        }
    }

}