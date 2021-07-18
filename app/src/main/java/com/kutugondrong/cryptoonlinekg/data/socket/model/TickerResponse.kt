package com.kutugondrong.cryptoonlinekg.data.socket.model

import com.google.gson.annotations.SerializedName

data class TickerResponse(
    @SerializedName("TYPE")
    val type: Int,
    @SerializedName("MARKET")
    val market: String,
    @SerializedName("FROMSYMBOL")
    val fromSymbol: String,
    @SerializedName("TOSYMBOL")
    val toSymbol: String,
    @SerializedName("PRICE")
    val price: String,
) {
    val symbol get() = "2~${market}~${fromSymbol}~${toSymbol}"
}
