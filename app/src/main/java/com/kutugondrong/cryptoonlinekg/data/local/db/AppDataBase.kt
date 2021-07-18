package com.kutugondrong.cryptoonlinekg.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kutugondrong.cryptoonlinekg.data.local.dao.CryptoDao
import com.kutugondrong.cryptoonlinekg.data.local.dao.RemoteKeysDao
import com.kutugondrong.cryptoonlinekg.data.local.entity.Crypto
import com.kutugondrong.cryptoonlinekg.data.local.entity.RemoteKeys


@Database(
    entities = [Crypto::class, RemoteKeys::class],
    version = 1, exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract val cryptoDao: CryptoDao
    abstract val remoteKeysDao: RemoteKeysDao

    companion object {
        @Volatile
        private var instance: AppDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance ?: buildDatabase(
                    context
                ).also {
                    instance = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "crypto_db"
            ).fallbackToDestructiveMigration()
                .build()
    }
}