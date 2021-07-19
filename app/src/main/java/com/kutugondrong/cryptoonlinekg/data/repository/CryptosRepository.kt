package com.kutugondrong.cryptoonlinekg.data.repository

import androidx.paging.*
import com.kutugondrong.cryptoonlinekg.data.db.AppDataBase
import com.kutugondrong.data.local.entity.Crypto
import com.kutugondrong.cryptoonlinekg.data.remotediator.CryptosRemoteMediator
import com.kutugondrong.data.remote.service.CryptoApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptosRepository @Inject constructor(
    private val cryptoApi: CryptoApi,
    private val db: AppDataBase
) {

    private val pagingSourceFactory = { db.cryptoDao.getCryptos() }

    @ExperimentalPagingApi
    fun getCryptos(): Flow<PagingData<Crypto>> {
        return Pager(
            config = PagingConfig(
                pageSize = CryptoApi.PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = CryptosRemoteMediator(
                cryptoApi,
                db
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun getSymbolRandom(): List<String> {
        return db.cryptoDao.getSymbolRandom()
    }
}