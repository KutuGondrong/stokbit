package com.kutugondrong.data.socket

import com.kutugondrong.data.socket.model.Subscription
import com.kutugondrong.data.socket.model.TickerResponse
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow

interface CryptoSocket {

    @Receive
    fun observeResponse(): Flow<TickerResponse>

    @Send
    fun subscribe(request: Subscription): Boolean

}

