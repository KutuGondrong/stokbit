package com.kutugondrong.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kutugondrong.data.local.entity.Crypto
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleCryptos(list: List<Crypto>)

    @Query("SELECT * FROM crypto")
    fun getCryptos(): PagingSource<Int, Crypto>

    @Query("DELETE FROM crypto")
    suspend fun clearCryptos()

    @Query("SELECT COUNT(id) from crypto")
    suspend fun count(): Int

    @Query("SELECT symbol from crypto ORDER BY RANDOM() LIMIT 1")
    suspend fun getSymbolRandom(): List<String>

    @Query("UPDATE crypto SET price=:price WHERE symbol = :symbols")
    suspend fun update(price: String, symbols: String)

}