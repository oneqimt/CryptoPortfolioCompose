package com.imtmobileapps.cryptocompose.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtmobileapps.cryptocompose.data.CryptoRepositoryImpl
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.util.RequestState
import com.imtmobileapps.cryptocompose.util.Routes
import com.imtmobileapps.cryptocompose.util.SearchAppBarState
import com.imtmobileapps.cryptocompose.util.getDummyCoin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import logcat.logcat
import javax.inject.Inject


@HiltViewModel
class ManageHoldingsViewModel @Inject constructor(
    private val repository: CryptoRepositoryImpl
) : ViewModel() {

    private val _searchState: MutableState<SearchAppBarState> = mutableStateOf(SearchAppBarState.CLOSED)
    val searchState: State<SearchAppBarState> = _searchState

    private val _searchTextState: MutableState<String> = mutableStateOf("")
    val searchTextState: State<String> = _searchTextState

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _allCoins =
        MutableStateFlow<RequestState<MutableList<Coin>>>(RequestState.Idle)
    val allCoins: StateFlow<RequestState<MutableList<Coin>>> = _allCoins

    var sortedList = MutableLiveData<List<Coin>>()
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

    fun onEvent(event : UIEvent){

        when (event){
            is UIEvent.Navigate ->{
                sendUiEvent(UIEvent.Navigate(event.route))
            }
            else -> {}
        }

    }

    fun filterListForSearch() {

        val allCoinsList = (allCoins.value as RequestState.Success).data

        println("$TAG DENNIS and searchTextState is ${searchTextState.value}")

        searchCoinList.clear()

        allCoinsList.let {
            //sortedList = it.sortedWith(compareBy(Coin::coinName))
            for (coin in it.listIterator()) {
                if (coin.coinName?.startsWith(searchTextState.value) == true) {
                    println("$TAG DENNIS and coin to add is: ${coin.coinName}")
                    searchCoinList.add(coin)

                }
            }

           sortedList.value = searchCoinList.toMutableList()

            logcat(TAG){ "${sortedList.value}"}

        }
    }

    private fun fetchAllCoinsFromRemote() {

        _allCoins.value = RequestState.Loading

        try {
            viewModelScope.launch {

                repository.getAllCoins().collect {
                    _allCoins.value = RequestState.Success(it).data
                    //sendUiEvent(ListEvent.OnAppInit(personId))
                }


            }
        } catch (e: Exception) {
            _allCoins.value = RequestState.Error(e.localizedMessage as String)
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