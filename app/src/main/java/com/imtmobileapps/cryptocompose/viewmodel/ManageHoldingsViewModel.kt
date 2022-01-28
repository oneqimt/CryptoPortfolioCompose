package com.imtmobileapps.cryptocompose.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtmobileapps.cryptocompose.data.CryptoRepositoryImpl
import com.imtmobileapps.cryptocompose.event.ListEvent
import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.util.CoinSort
import com.imtmobileapps.cryptocompose.util.DataSource
import com.imtmobileapps.cryptocompose.util.RequestState
import com.imtmobileapps.cryptocompose.util.sortCryptoValueList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ManageHoldingsViewModel @Inject constructor(
    private val repository: CryptoRepositoryImpl
) : ViewModel() {


    val searchTextState: MutableState<String> = mutableStateOf("")

    private val _allCoins =
        MutableStateFlow<RequestState<List<Coin>>>(RequestState.Idle)
    val allCoins: StateFlow<RequestState<List<Coin>>> = _allCoins

    init{
        fetchAllCoinsFromRemote()
    }

    private fun fetchAllCoinsFromRemote() {

        _allCoins.value = RequestState.Loading

        try {
            viewModelScope.launch {

                repository.getAllCoins().collect {
                    _allCoins.value = RequestState.Success(it).data
                    //sendUiEvent(ListEvent.OnAppInit(personId))
                }

                val allCoinsList = (allCoins.value as RequestState.Success<List<Coin>>).data

                // SET A DEFAULT SORT on the LIST
                // TODO update this sort method
                //val sortedlist = sortCryptoValueList(allCoinsList, CoinSort.NAME)
                //_allCoins.value = RequestState.Success(sortedlist)

                // Call deleteAllCoins on database first (prices vary tremendously)
               // deleteAllCoins()
               // storeCoinsInDatabase(personCoinsList)
                // initial sort state
                //saveSortState(CoinSort.NAME)

                //val sortedList = updatedList.sortedWith(compareBy(Coin::cmcRank))
            }
        } catch (e: Exception) {
            _allCoins.value = RequestState.Error(e.localizedMessage as String)
        }

    }

    companion object {
        private val TAG = ManageHoldingsViewModel::class.java.simpleName
    }


}