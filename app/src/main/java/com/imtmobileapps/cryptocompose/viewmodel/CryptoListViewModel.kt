package com.imtmobileapps.cryptocompose.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtmobileapps.cryptocompose.data.CryptoRepositoryImpl
import com.imtmobileapps.cryptocompose.data.local.DataStoreHelperImpl
import com.imtmobileapps.cryptocompose.event.ListEvent
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.TotalValues
import com.imtmobileapps.cryptocompose.util.Action
import com.imtmobileapps.cryptocompose.util.RequestState
import com.imtmobileapps.cryptocompose.util.Routes
import com.imtmobileapps.cryptocompose.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val repository: CryptoRepositoryImpl

    ) : ViewModel() {

    // Backing property to avoid state updates from other classes
    // https://developer.android.com/kotlin/flow/stateflow-and-sharedflow
    private val _personCoins =
        MutableStateFlow<RequestState<List<CryptoValue>>>(RequestState.Idle)

    // The UI collects from this StateFlow to get its state updates
    val personCoins: StateFlow<RequestState<List<CryptoValue>>> = _personCoins

    private val _totalValues = MutableStateFlow<RequestState<TotalValues?>>(RequestState.Idle)
    val totalValues: StateFlow<RequestState<TotalValues?>> = _totalValues

    // This in not wrapped with RequestState because it is local
    private val _selectedCryptoValue: MutableStateFlow<CryptoValue?> = MutableStateFlow(null)
    val selectedCryptoValue: StateFlow<CryptoValue?> = _selectedCryptoValue

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

        // APP BARS
    var searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")
    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)
    // SEARCH
    private val _searchedCoins =
        MutableStateFlow<RequestState<List<CryptoValue>>>(RequestState.Idle)
    val searchedCoins: StateFlow<RequestState<List<CryptoValue>>> = _searchedCoins

    private val _sortState =
        MutableStateFlow<RequestState<CryptoValue>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<CryptoValue>> = _sortState

    // NOTE : personId will come from login layer when it is built
    // And these calls probably will be made from LoginComposable
    init {
        fetchCoinsFromRemote(3)
        fetchTotalValuesFromRemote(3)
    }

    fun savePersonId(personId: Int){
        try{
            viewModelScope.launch {
                repository.savePersonId(personId)
                println("$TAG SAVING personId $personId")
            }
        }catch (e: Exception){
            println("$TAG SAVING person ERROR ${e.localizedMessage}")
        }

    }

    fun getPersonId():Int{

        var personId : Int = -1

        viewModelScope.launch {
            repository.getCurrentPersonId().collect {
                personId = it
                println("$TAG GETTING personId $it")
            }
        }
        return personId
    }

    fun onEvent(event: UIEvent) {
        when (event) {
            UIEvent.PopBackStack -> {
                println("$TAG UIEvent.PopBackStack")
            }

            is ListEvent.OnListRefresh -> {
                println("$TAG ListEvent.OnListRefresh")
                fetchCoinsFromRemote(1)
            }

            is ListEvent.OnCoinClicked -> {
                val personId = getPersonId()
                println("$TAG ListEvent.OnCoinClicked and PERSON ID  is: $personId")
                _selectedCryptoValue.value = event.cryptoValue
                val route = Routes.PERSON_COINS_DETAIL + "?cmcId=${event.cryptoValue.coin.cmcId}"
                println("$TAG ListEvent.OnCoinClicked NAVIGATE to this route:  $route")
                sendUiEvent(UIEvent.Navigate(route))

            }
            else -> Unit
        }

    }

    private fun fetchCoinsFromRemote(personId: Int) {

        _personCoins.value = RequestState.Loading

        try {
            viewModelScope.launch {

                repository.getPersonCoins(personId).collect {
                    _personCoins.value = RequestState.Success(it).data
                    sendUiEvent(ListEvent.OnAppInit(personId))
                }

                // TEST ONLY STORE LOCALLY. But, probably not here
                /*val listToCache = (personCoins.value as RequestState.Success<List<CryptoValue>>).data
                storeCoinsInDatabase(listToCache)*/
            }
        } catch (e: Exception) {
            _personCoins.value = RequestState.Error(e.localizedMessage as String)
        }

    }

    private fun fetchTotalValuesFromRemote(personId: Int) {
        _totalValues.value = RequestState.Loading
        try {

            viewModelScope.launch {
                repository.getTotalValues(personId).collect {
                    _totalValues.value = RequestState.Success(it).data
                }
            }



        } catch (e: Exception) {
            _totalValues.value = RequestState.Error(e.localizedMessage as String)
        }
    }

    private fun storeCoinsInDatabase(list: List<CryptoValue>) {
        try{

            viewModelScope.launch {
                repository.insertAll(list).collect {
                    println("$TAG INSERT ALL and result is $it")
                }
            }

        }catch (e: Exception){

        }
    }

    fun searchDatabase(searchQuery: String) {
        _searchedCoins.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase(searchQuery = "%$searchQuery%")
                    .collect {
                        _searchedCoins.value = RequestState.Success(it)

                        println("$TAG in searchDatabase and coins are : ${searchedCoins.value}" )
                    }
            }
        } catch (e: Exception) {
            _searchedCoins.value = RequestState.Error(e.localizedMessage as String)
        }
        searchAppBarState.value = SearchAppBarState.TRIGGERED
    }


    fun persistSortState(cryptoValue: CryptoValue) {
        viewModelScope.launch(Dispatchers.IO) {
        // TODO create DataStore
        // dataStore.persistSortState(cryptoValue = cryptoValue)
        }
    }
    private fun readSortState() {
        Log.d("readSortState", "")
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
              // TODO build data store layer
               /* dataStore.readSortState
                    .map { CryptoValue.valueOf(it) }
                    .collect {
                        _sortState.value = RequestState.Success(it)
                    }*/
            }
        } catch (e: Exception) {
            _sortState.value = RequestState.Error(e.localizedMessage as String)
        }
    }


    private fun sendUiEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }


    companion object {
        private val TAG = CryptoListViewModel::class.java.simpleName
    }

}



