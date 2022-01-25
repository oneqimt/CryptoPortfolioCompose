package com.imtmobileapps.cryptocompose.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.imtmobileapps.cryptocompose.model.CryptoValue

@Dao
interface CryptoValuesDao {

    @Insert
    suspend fun insertAll(vararg coins: CryptoValue) : List<Long>

    @Query(value = "SELECT * FROM cryptovalue")
    suspend fun getPersonCoins(): List<CryptoValue>

    @Query(value = "SELECT * FROM cryptovalue WHERE uuid = :cryptoId")
    suspend fun getCoin(cryptoId: Int):CryptoValue

    @Query(value = "DELETE FROM cryptovalue")
    suspend fun deleteAllCoins()

    @Query("SELECT * FROM cryptovalue WHERE coin_name LIKE :searchQuery OR coin_symbol LIKE :searchQuery")
    suspend fun searchDatabase(searchQuery: String): List<CryptoValue>

}