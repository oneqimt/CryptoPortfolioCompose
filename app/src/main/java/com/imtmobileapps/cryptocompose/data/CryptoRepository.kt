package com.imtmobileapps.cryptocompose.data

import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.TotalValues
import com.imtmobileapps.cryptocompose.util.CoinSort
import com.imtmobileapps.cryptocompose.util.DataSource
import com.imtmobileapps.cryptocompose.util.RequestState
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {

    fun getPersonCoins(personId : Int, dataSource: DataSource): Flow<RequestState<List<CryptoValue>>>

    fun getAllCoins(): Flow<RequestState<List<Coin>>>

    fun getCoin(coinName : String): Flow<CryptoValue>

    fun getTotalValues(personId: Int): Flow<RequestState<TotalValues>>

    fun searchDatabase(searchQuery: String): Flow<List<CryptoValue>>

    fun insertAll(list : List<CryptoValue>) : Flow<List<Long>>

    suspend fun insertTotalValues(totalValues: TotalValues): Long

    suspend fun deleteAllCoins()

    suspend fun savePersonId(personId: Int)

    suspend fun getCurrentPersonId() : Flow<Int>

    suspend fun deleteTotalValues()

    suspend fun saveSortState(sortState: CoinSort)

    fun getSortState(): Flow<String>

    suspend fun saveUpdateTime(updateTime: Long)

    fun getUpdateTime(): Flow<Any>

    suspend fun saveCacheDuration(cacheDuration: String)

    fun getCacheDuration(): Flow<String>

}