package com.imtmobileapps.cryptocompose.data.local

import com.imtmobileapps.cryptocompose.util.CoinSort

interface DataStoreHelper {

    suspend fun savePersonId(personId: Int)

    suspend fun saveUpdateTime(time: Long)

    suspend fun saveCacheDuration(cacheDuration : String)

    suspend fun saveSortState(coinSort: CoinSort)



}