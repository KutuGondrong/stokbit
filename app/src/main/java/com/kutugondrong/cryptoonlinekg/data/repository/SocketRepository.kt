package com.kutugondrong.cryptoonlinekg.data.repository

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.kutugondrong.data.socket.CryptoSocket
import com.kutugondrong.data.socket.model.Subscription
import com.kutugondrong.data.socket.model.TickerResponse
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketRepository @Inject constructor(
    private val cryptoSocket: CryptoSocket
) {

    suspend fun observeCryptoSocket(update: (TickerResponse) -> Unit){
        cryptoSocket.observeResponse().collectLatest { response ->
            update(response)
        }
    }

    suspend fun subscribeFirst(listSubs : List<String>) {
        cryptoSocket.observeWebSocketEvent().filter {it is WebSocket.Event.OnConnectionOpened<*>}.collect {
            cryptoSocket.subscribe(Subscription(subs = listSubs))
        }
    }

    fun subscribe(listSubs : List<String>) {
        cryptoSocket.subscribe(Subscription(subs = listSubs))
    }

}