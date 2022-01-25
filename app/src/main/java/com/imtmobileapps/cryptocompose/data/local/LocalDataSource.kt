package com.imtmobileapps.cryptocompose.data.local

import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.TotalValues
import com.imtmobileapps.cryptocompose.util.DataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val dataStore: DataStoreHelperImpl,
    private val cryptoValuesDao:CryptoValuesDao,
    private val personDao: PersonDao,
    private val totalValuesDao: TotalValuesDao

) {

    suspend fun searchDatabase(searchQuery: String) : List<CryptoValue> {
        return cryptoValuesDao.searchDatabase(searchQuery = searchQuery)
    }

    suspend fun insertAll(vararg coins: CryptoValue) : List<Long>{
        return cryptoValuesDao.insertAll(*coins)
    }

    suspend fun deleteAllCoins(){
        cryptoValuesDao.deleteAllCoins()
    }

    suspend fun getPersonCoins(personId: Int, dataSource: DataSource) : List<CryptoValue>{
        return cryptoValuesDao.getPersonCoins()
    }

    suspend fun savePersonId(personId : Int){
        dataStore.savePersonId(personId)
    }

    fun getPersonId() : Flow<Int> {
        return dataStore.personId
    }

    suspend fun insertTotalValues(totalValues: TotalValues): Long{
        return totalValuesDao.insertTotalValues(totalValues)
    }

    suspend fun deleteTotalValues(){
        totalValuesDao.deleteAllTotalValues()
    }

}