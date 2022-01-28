package com.imtmobileapps.cryptocompose.data.remote

import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.TotalValues
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoApi {

    @GET("managecoins?action=personcmccoins")
    suspend fun getPersonCoins(@Query("person_id") person_id: Int): List<CryptoValue>

    @GET("totals")
    suspend fun getTotals(@Query("person_id") person_id: Int): TotalValues

    @GET("managecoins?action=cmccoins")
    suspend fun getAllCoins() : List<Coin>

}