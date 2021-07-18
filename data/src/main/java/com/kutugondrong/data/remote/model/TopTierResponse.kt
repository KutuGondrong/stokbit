package com.kutugondrong.data.remote.model

import com.google.gson.annotations.SerializedName

data class TopTierResponse(
    @SerializedName("MetaData")
    val metaData: MetaDataResponse,
    @SerializedName("Data")
    val data: List<DataResponse>
)

class MetaDataResponse(val Count: Int)

class DataResponse(
    @SerializedName("CoinInfo")
    val coinInfo: CoinInfoResponse,
    @SerializedName("RAW")
    val raw: RawResponse
) {
    val symbol get() = "2~${raw.usd.lastMarket}~${raw.usd.fromSymbol}~${raw.usd.toSymbol}"
}


class CoinInfoResponse(
    @SerializedName("Id")
    val id: String,
    @SerializedName("FullName")
    val fullName: String,
    @SerializedName("ImageUrl")
    val imageUrl: String,
    @SerializedName("Name")
    val name: String,
)

class RawResponse(
    @SerializedName("USD")
    val usd: USDResponse
)

class USDResponse(
    @SerializedName("LASTMARKET")
    val lastMarket: String,
    @SerializedName("FROMSYMBOL")
    val fromSymbol: String,
    @SerializedName("TOSYMBOL")
    val toSymbol: String,
    @SerializedName("PRICE")
    val price: String,
    @SerializedName("TYPE")
    val type: String,
)
