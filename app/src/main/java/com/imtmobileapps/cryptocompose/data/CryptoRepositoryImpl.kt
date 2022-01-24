package com.imtmobileapps.cryptocompose.data

import com.imtmobileapps.cryptocompose.data.local.LocalDataSource
import com.imtmobileapps.cryptocompose.data.remote.RemoteDataSource
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.TotalValues
import com.imtmobileapps.cryptocompose.util.Constants.CMC_LOGO_URL
import com.imtmobileapps.cryptocompose.util.RequestState
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class CryptoRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
    ) : CryptoRepository {


    override fun getPersonCoins(personId: Int): Flow<RequestState<List<CryptoValue>>> {
        return flow {

            // TODO add checkDurationCache logic,
            //  if exceeded -> call remoteDataSource else -> call localDataSource

            val personCoins = remoteDataSource.getPersonCoins(personId)
            // TODO, We should not have to do this transformation on the client, I will add to the server
            personCoins.map {
                val logo = CMC_LOGO_URL + it.coin.cmcId + ".png"
                it.coin.smallCoinImageUrl = logo
                it.coin.largeCoinImageUrl = logo
            }
            emit(RequestState.Success(personCoins))

        }
    }

    override fun getTotalValues(personId: Int): Flow<RequestState<TotalValues>> {
         return flow{

             val totalValues = remoteDataSource.getTotalValues(personId)

             emit(RequestState.Success(totalValues))
         }
    }

    override fun searchDatabase(searchQuery: String): Flow<List<CryptoValue>> {

        return flow{
            val searchResult = localDataSource.searchDatabase(searchQuery = searchQuery)

            emit(RequestState.Success(searchResult).data)
        }
    }

    override fun insertAll(list: List<CryptoValue>): Flow<List<Long>> {
        return flow {

            var insertResult = localDataSource.insertAll(*list.toTypedArray())

            emit(RequestState.Success(insertResult).data)
        }
    }

    override suspend fun savePersonId(personId: Int) {
        localDataSource.savePersonId(personId)
    }

    override suspend fun getCurrentPersonId(): Flow<Int> {
        return localDataSource.getPersonId()
    }
}

