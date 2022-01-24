package com.imtmobileapps.cryptocompose.data.local

interface DataStoreHelper {

    suspend fun savePersonId(personId: Int)

    suspend fun saveUpdateTime(time: Long)

    suspend fun saveCacheDuration(cacheDuration : String)

}