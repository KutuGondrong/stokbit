package com.kutugondrong.cryptoonlinekg.data.repository

import com.kutugondrong.data.socket.CryptoSocket
import com.kutugondrong.data.socket.model.Subscription
import com.kutugondrong.data.socket.model.TickerResponse
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketRepository @Inject constructor(
    private val cryptoSocket: CryptoSocket
) {

    suspend fun observeCryptoSocket(update: (TickerResponse) -> Unit){
        cryptoSocket.subscribe(Subscription(subs = emptyList()))
        cryptoSocket.observeResponse().collectLatest { response ->
            update(response)
        }
    }

    fun subscribe() {
        cryptoSocket.subscribe(Subscription(subs =  listOf("2~Coinbase~BTC~USD")))
    }
}