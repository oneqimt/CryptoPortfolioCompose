package com.imtmobileapps.cryptocompose.data

import com.imtmobileapps.cryptocompose.model.*
import com.imtmobileapps.cryptocompose.util.CoinSort
import com.imtmobileapps.cryptocompose.util.DataSource
import com.imtmobileapps.cryptocompose.util.RequestState
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {

    fun login(uname: String, pass: String): Flow<SignUp>
    suspend fun logout() : Boolean

    fun resetPassword(email: String): Flow<ReturnDTO>
    fun signUp(signUp: SignUp): Flow<SignUp>

    fun addHolding(coinHolding: CoinHolding): Flow<Holdings>
    fun deleteHolding(holdings: Holdings): Flow<Holdings>
    fun updateHolding(coinHolding: CoinHolding): Flow<Holdings>

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