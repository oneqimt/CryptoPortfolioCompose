package com.imtmobileapps.cryptocompose.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtmobileapps.cryptocompose.data.CryptoRepositoryImpl
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.util.DataType
import com.imtmobileapps.cryptocompose.util.RequestState
import com.imtmobileapps.cryptocompose.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ManageHoldingsViewModel @Inject constructor(
    private val repository: CryptoRepositoryImpl,
) : ViewModel() {

    private val _searchState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchState: State<SearchAppBarState> = _searchState

    private val _searchTextState: MutableState<String> = mutableStateOf("")
    val searchTextState: State<String> = _searchTextState

    private val _dataType: MutableState<String> = mutableStateOf(DataType.NONE)
    val dataType: State<String> = _dataType

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _allCoins =
        MutableStateFlow<RequestState<MutableList<Coin>>>(RequestState.Idle)
    val allCoins: StateFlow<RequestState<MutableList<Coin>>> = _allCoins

    private val _filteredCoins =
        MutableStateFlow<RequestState<MutableList<Coin>>>(RequestState.Idle)
    val filteredCoins: StateFlow<RequestState<MutableList<Coin>>> = _filteredCoins

    private var searchCoinList: MutableList<Coin> = mutableListOf()

    init {
        fetchAllCoinsFromRemote()
    }

    fun updateSearchState(newVal: SearchAppBarState) {
        _searchState.value = newVal
    }

    fun updateSearchTextState(newVal: String) {
        _searchTextState.value = newVal
    }

    fun onEvent(event: UIEvent) {

        when (event) {
            is UIEvent.Navigate -> {
                sendUiEvent(UIEvent.Navigate(event.route))
            }
            else -> {}
        }

    }

    fun filterListForSearch() {

        _dataType.value = DataType.FILTERED_COINS

        val allCoinsList = (allCoins.value as RequestState.Success).data

        println("$TAG DENNIS and searchTextState is ${searchTextState.value}")
        // clear _filteredCoins
        _filteredCoins.value = RequestState.Success(mutableStateListOf())
        searchCoinList.clear()

        allCoinsList.let {
            //sortedList = it.sortedWith(compareBy(Coin::coinName))
            for (coin in it.listIterator()) {

                if (coin.coinName?.startsWith(searchTextState.value, true)  == true) {
                    //println("$TAG DENNIS and coin to add is: ${coin.coinName} ${coin.cmcRank}")
                    searchCoinList.add(coin)
                }
            }
            val temp = RequestState.Success(searchCoinList.toMutableStateList())
            _filteredCoins.value = RequestState.Success(temp).data
        }
    }

    fun fetchAllCoinsFromRemote() {
        _dataType.value = DataType.ALL_COINS
        _allCoins.value = RequestState.Loading
        _filteredCoins.value = RequestState.Loading

        try {
            viewModelScope.launch {

                repository.getAllCoins().collect {
                    _allCoins.value = RequestState.Success(it).data
                    _filteredCoins.value = RequestState.Success(it).data
                    //sendUiEvent(ListEvent.OnAppInit(personId))
                }


            }
        } catch (e: Exception) {
            _allCoins.value = RequestState.Error(e.localizedMessage as String)
            _filteredCoins.value = RequestState.Error(e.localizedMessage as String)
        }

    }

    private fun sendUiEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    companion object {
        private val TAG = ManageHoldingsViewModel::class.java.simpleName
    }


}