package com.imtmobileapps.cryptocompose.data

import com.imtmobileapps.cryptocompose.data.local.CryptoValuesDao
import com.imtmobileapps.cryptocompose.data.local.PersonDao
import com.imtmobileapps.cryptocompose.data.local.TotalValuesDao
import com.imtmobileapps.cryptocompose.data.remote.CryptoApi
import com.imtmobileapps.cryptocompose.data.remote.RemoteDataSource
import com.imtmobileapps.cryptocompose.model.CryptoValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CryptoRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val cryptoValuesDao: CryptoValuesDao,
    private val personDao: PersonDao,
    private val totalValuesDao: TotalValuesDao
    ) : CryptoRepository {


    override fun getPersonCoins(personId: Int): List<CryptoValue> {
        return emptyList()
    }
}

