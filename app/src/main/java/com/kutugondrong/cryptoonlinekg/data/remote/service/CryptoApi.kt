package com.kutugondrong.cryptoonlinekg.data.remote.service

import com.kutugondrong.cryptoonlinekg.data.remote.model.TopTierResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoApi {

    companion object {
        const val TSYM = "USD"
        const val PAGE_SIZE = 20
    }

    @GET("data/top/totaltoptiervolfull?tsym=$TSYM&limit=$PAGE_SIZE")
    suspend fun getTopTier(@Query("page") page: Int): TopTierResponse
}