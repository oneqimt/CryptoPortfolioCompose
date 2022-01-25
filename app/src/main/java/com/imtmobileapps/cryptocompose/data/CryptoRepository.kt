package com.imtmobileapps.cryptocompose.data

import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.TotalValues
import com.imtmobileapps.cryptocompose.util.DataSource
import com.imtmobileapps.cryptocompose.util.RequestState
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {

    fun getPersonCoins(personId : Int, dataSource: DataSource): Flow<RequestState<List<CryptoValue>>>

    fun getTotalValues(personId: Int): Flow<RequestState<TotalValues>>

    fun searchDatabase(searchQuery: String): Flow<List<CryptoValue>>

    fun insertAll(varargs : List<CryptoValue>) : Flow<List<Long>>

    suspend fun insertTotalValues(totalValues: TotalValues): Long

    suspend fun deleteAllCoins()

    suspend fun savePersonId(personId: Int)

    suspend fun getCurrentPersonId() : Flow<Int>

    suspend fun deleteTotalValues()

}