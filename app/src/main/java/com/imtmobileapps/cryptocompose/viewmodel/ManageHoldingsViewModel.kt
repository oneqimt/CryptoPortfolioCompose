package com.imtmobileapps.cryptocompose.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtmobileapps.cryptocompose.data.CryptoRepositoryImpl
import com.imtmobileapps.cryptocompose.event.ListEvent
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.util.DataType
import com.imtmobileapps.cryptocompose.util.RequestState
import com.imtmobileapps.cryptocompose.util.Routes
import com.imtmobileapps.cryptocompose.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import logcat.LogPriority
import logcat.logcat
import javax.inject.Inject


@HiltViewModel
class ManageHoldingsViewModel @Inject constructor(
    private val repository: CryptoRepositoryImpl
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

    // This in not wrapped with RequestState because it is local
    private val _selectedCoin: MutableStateFlow<Coin?> = MutableStateFlow(null)
    val selectedCoin: StateFlow<Coin?> = _selectedCoin

    private val _selectedCryptoValue: MutableStateFlow<CryptoValue?> = MutableStateFlow(value = null)
    val selectedCryptoValue : StateFlow<CryptoValue?> = _selectedCryptoValue

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
            is ListEvent.OnAllCoinClicked -> {
                //logcat(LogPriority.INFO) { "OnAllCoinClicked and coin is: ${event.coin}" }
                _selectedCoin.value = event.coin
                // get the associated CryptoValue, if it is null, it means the user does NOT have that coin yet
                event.coin.coinName?.let { getSelectedCryptoValue(it) }
                val route = Routes.ADD_HOLDING_DETAIL
                sendUiEvent(UIEvent.Navigate(route))
            }
            else -> {}
        }

    }

    fun filterListForSearch() {

        _dataType.value = DataType.FILTERED_COINS
        val allCoinsList = (allCoins.value as RequestState.Success).data
        println("$TAG and searchTextState is ${searchTextState.value}")
       // clear the lists
        _filteredCoins.value = RequestState.Success(mutableStateListOf())
        searchCoinList.clear()

        allCoinsList.let {

            for (coin in it.listIterator()) {

                if (coin.coinName?.startsWith(searchTextState.value, true) == true) {
                    searchCoinList.add(coin)
                }
            }
            val temp = RequestState.Success(searchCoinList.toMutableStateList())
            _filteredCoins.value = RequestState.Success(temp).data
        }
    }

    private fun getSelectedCryptoValue(coinName: String){
        try {
            viewModelScope.launch {
                repository.getCoin(coinName).collect {
                    _selectedCryptoValue.value = it
                    logcat(TAG){"selectedCryptoValue is: ${selectedCryptoValue.value}"}
                }
            }
        }catch (e: Exception){
            logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
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