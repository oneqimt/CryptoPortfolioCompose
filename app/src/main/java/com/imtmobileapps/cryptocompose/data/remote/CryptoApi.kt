package com.imtmobileapps.cryptocompose.data.remote

import com.imtmobileapps.cryptocompose.model.*
import retrofit2.http.*

interface CryptoApi {

    @GET("managecoins?action=personcmccoins")
    suspend fun getPersonCoins(@Query("person_id") person_id: Int): List<CryptoValue>

    @GET("totals")
    suspend fun getTotals(@Query("person_id") person_id: Int): TotalValues

    @GET("managecoins?action=cmccoins")
    suspend fun getAllCoins() : MutableList<Coin>

    @POST("holdings?action=addholding")
    fun addHolding(@Body coinHolding: CoinHolding) : Holdings

    @POST("holdings?action=deleteholding")
    fun deleteHolding(@Body holdings: Holdings) : Holdings

    @POST("holdings?action=updateholding")
    fun updateHolding(@Body coinHolding: CoinHolding) : Holdings

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("uname") uname: String?,
        @Field("pass") pass: String?
    ): SignUp

    @FormUrlEncoded
    @POST("resetpassword")
    fun resetPassword(
        @Field("email") email: String?
    ): ReturnDTO

    @POST("logout")
    fun logout() : Boolean

    @POST("signup")
    fun signUp(@Body signUp: SignUp): SignUp

}