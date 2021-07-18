package com.kutugondrong.cryptoonlinekg.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crypto")
data class Crypto(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val fullName: String,
    val name: String,
    val price: String,
    val imgUrl: String,
    val lastMarket: String,
    val fromSymbol: String,
    val toSymbol: String,
    val type: String,
    val symbol: String,
) {

    val fullUrlImage get() = "https://www.cryptocompare.com$imgUrl"
}