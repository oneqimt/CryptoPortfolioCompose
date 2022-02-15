package com.imtmobileapps.cryptocompose.data.remote

import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.TotalValues
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val cryptoApi: CryptoApi
) {

    suspend fun getPersonCoins(personId : Int): List<CryptoValue> {
        return cryptoApi.getPersonCoins(personId)
    }

    suspend fun getTotalValues(personId: Int) : TotalValues{
       return cryptoApi.getTotals(personId)
    }

    suspend fun getAllCoins(): MutableList<Coin>{
        return cryptoApi.getAllCoins()
    }


}