package com.imtmobileapps.cryptocompose.data

import com.imtmobileapps.cryptocompose.model.CryptoValue
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {

    fun getPersonCoins(personId : Int): List<CryptoValue>
}