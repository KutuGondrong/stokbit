package com.kutugondrong.cryptoonlinekg.data.remotediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kutugondrong.cryptoonlinekg.data.db.AppDataBase
import com.kutugondrong.data.local.entity.Crypto
import com.kutugondrong.data.local.entity.RemoteKeys
import com.kutugondrong.data.remote.service.CryptoApi
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class CryptosRemoteMediator(
    private val service: CryptoApi,
    private val db: AppDataBase
) : RemoteMediator<Int, Crypto>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Crypto>): MediatorResult {
        val key = when (loadType) {
            LoadType.REFRESH -> {
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                getKey()
            }
        }

        try {

            if (key != null) {
                if (key.isEndReached) return MediatorResult.Success(endOfPaginationReached = true)
            }

            val page: Int = key?.nextKey ?: STARTING_PAGE_INDEX
            val apiResponse = service.getTopTier(page)

            val cryptoList: List<Crypto> = apiResponse.data.mapNotNull {
                try {
                    Crypto(
                        it.coinInfo.id,
                        it.coinInfo.fullName,
                        it.coinInfo.name,
                        it.raw.usd.price,
                        it.coinInfo.imageUrl,
                        it.raw.usd.lastMarket,
                        it.raw.usd.fromSymbol,
                        it.raw.usd.toSymbol,
                        it.raw.usd.type,
                        it.symbol
                    )
                } catch (e: Exception) {
                    null
                }
            }

            val endOfPaginationReached = db.cryptoDao.count() == apiResponse.metaData?.Count
            db.withTransaction {

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                if (loadType == LoadType.REFRESH) {
                    db.cryptoDao.clearCryptos()
                }

                db.remoteKeysDao.insertKey(
                    RemoteKeys(
                        0,
                        prevKey = prevKey,
                        nextKey = nextKey,
                        isEndReached = endOfPaginationReached
                    )
                )
                db.cryptoDao.insertMultipleCryptos(cryptoList)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }


    private suspend fun getKey(): RemoteKeys? {
        return db.remoteKeysDao.getKeys().firstOrNull()
    }


    companion object {
        const val STARTING_PAGE_INDEX = 1
    }


}