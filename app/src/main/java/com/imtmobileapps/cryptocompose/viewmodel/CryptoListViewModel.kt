package com.imtmobileapps.cryptocompose.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtmobileapps.cryptocompose.data.CryptoRepositoryImpl
import com.imtmobileapps.cryptocompose.event.ListEvent
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.Person
import com.imtmobileapps.cryptocompose.model.TotalValues
import com.imtmobileapps.cryptocompose.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import logcat.LogPriority
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val repository: CryptoRepositoryImpl,
) : ViewModel() {

    private val _personId: MutableState<Int> = mutableStateOf(0)
    val personId: State<Int> = _personId

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

    private val _searchedCoins =
        MutableStateFlow<RequestState<List<CryptoValue>>>(RequestState.Idle)
    private val searchedCoins: StateFlow<RequestState<List<CryptoValue>>> = _searchedCoins

    //SORT
    private val _sortState =
        MutableStateFlow<RequestState<CoinSort>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<CoinSort>> = _sortState

    // UPDATE TIME
    // CACHE DURATION
    init {
        logcat(TAG){"INIT() called"}
    }

    fun login(uname: String, pass: String) {
        try {
            viewModelScope.launch {
                repository.login(uname, pass).collect { signUp ->
                    // save the Person object to the database
                    signUp.person.let { person ->
                        // save the personId Int to the dataStore
                        person.personId?.let {
                            repository.savePersonId(it)
                            _personId.value = it
                        }
                        savePerson(person)
                    }

                    fetchCoinsFromRemote(_personId.value)
                    fetchTotalValuesFromRemote(_personId.value)

                    sendUiEvent(UIEvent.Navigate(Routes.PERSON_COINS_LIST))

                }
            }
        } catch (e: Exception) {
            logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
        }
    }

    fun logout() {
        try {
            viewModelScope.launch {
                val loggedOut = repository.logout()
                logcat(TAG) { "LOGOUT and loggedOut is : $loggedOut" }
                // clear all existing values
               _personCoins.value = RequestState.Success(mutableListOf())
               _totalValues.value = RequestState.Success(getDummyTotalsValue())
               _searchedCoins.value = RequestState.Success(mutableListOf())

                clearDatabase()
                clearPersonId()
                sendUiEvent(UIEvent.Logout)

            }
        } catch (e: Exception) {
            logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
        }
    }

    private fun savePerson(person: Person){
        try{
            viewModelScope.launch {
                val cachedPerson = person.personId?.let { repository.getPerson(it) }
                if (cachedPerson == null) {
                    // clear person first
                    repository.deletePerson()
                    // then save
                    val result: Long = repository.savePerson(person)
                    person.personuuid = result.toInt()

                }else{
                    logcat(TAG){"That person already exists!"}
                }
            }
        }catch (e: Exception){
            logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
        }
    }

    private fun clearPersonId() {
        try {
            viewModelScope.launch {
                repository.savePersonId(0)
            }
        } catch (e: Exception) {
            logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
        }
    }

    private fun clearDatabase() {
        try {
            viewModelScope.launch {
                repository.deleteAllCoins()
                repository.deletePerson()
            }
        } catch (e: Exception) {
            logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
        }
    }
    fun onEvent(event: UIEvent) {
        when (event) {
            is ListEvent.OnCoinClicked -> {

                _selectedCryptoValue.value = event.cryptoValue
                val route = Routes.PERSON_COINS_DETAIL + "?cmcId=${event.cryptoValue.coin.cmcId}"
                println("$TAG ListEvent.OnCoinClicked NAVIGATE to this route:  $route")
                sendUiEvent(UIEvent.Navigate(route))
            }

            is ListEvent.OnAddCoinClicked -> {
                val route = Routes.ADD_HOLDING_LIST
                println("$TAG ListEvent.OnAddCoinClicked NAVIGATE to this route:  $route")
                sendUiEvent(UIEvent.Navigate(route))
            }

            else -> Unit
        }
    }

    private fun fetchCoinsFromRemote(personId: Int) {

        _personCoins.value = RequestState.Loading

        try {
            viewModelScope.launch {

                repository.getPersonCoins(personId, DataSource.REMOTE).collect {
                    _personCoins.value = RequestState.Success(it).data
                    sendUiEvent(ListEvent.OnAppInit(personId))
                }

                val personCoinsList =
                    (personCoins.value as RequestState.Success<List<CryptoValue>>).data
                // SET A DEFAULT SORT on the LIST
                val sortedlist = sortCryptoValueList(personCoinsList, CoinSort.NAME)
                _personCoins.value = RequestState.Success(sortedlist)

                // Call deleteAllCoins on database first (prices vary tremendously)
                deleteAllCoins()
                storeCoinsInDatabase(personCoinsList)
                // initial sort state
                saveSortState(CoinSort.NAME)
            }
        } catch (e: Exception) {
            _personCoins.value = RequestState.Error(e.localizedMessage as String)
        }

    }

    fun fetchCoinsFromDatabase(personId: Int) {
        try {
            viewModelScope.launch {
                repository.getPersonCoins(personId, DataSource.LOCAL).collect {
                    _personCoins.value = RequestState.Success(it).data

                    val personcoinslist =
                        (personCoins.value as RequestState.Success<List<CryptoValue>>).data
                    val sortedlist = sortCryptoValueList(personcoinslist, CoinSort.NAME)
                    _personCoins.value = RequestState.Success(sortedlist)

                    println("$TAG COINS from DATABASE are: ${personCoins.value}")
                }
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

                // Delete existing TotalValues
                deleteTotalValues()

                val totalsToStore = (totalValues.value as RequestState.Success<*>).data

                println("$TAG TotalValues that are stored in DB $totalsToStore")
                storeTotalValuesInDatabase(totalsToStore as TotalValues)
            }

        } catch (e: Exception) {
            _totalValues.value = RequestState.Error(e.localizedMessage as String)
        }
    }

    private fun fetchTotalValuesFromDatabase() {
        _totalValues.value = RequestState.Loading

        try {
            viewModelScope.launch {
                repository.getTotalValues(1).collect {
                    _totalValues.value = RequestState.Success(it).data
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun storeCoinsInDatabase(list: List<CryptoValue>) {
        try {

            viewModelScope.launch {
                repository.insertAll(list).collect {
                    println("$TAG INSERT ALL and result is $it")
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun storeTotalValuesInDatabase(totalValues: TotalValues) {
        try {
            viewModelScope.launch {
                repository.insertTotalValues(totalValues)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteAllCoins() {
        try {
            viewModelScope.launch {
                repository.deleteAllCoins()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteTotalValues() {
        try {
            viewModelScope.launch {
                repository.deleteTotalValues()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun searchDatabase(searchQuery: String) {
        _searchedCoins.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase(searchQuery = "%$searchQuery%")
                    .collect {

                        _searchedCoins.value = RequestState.Success(it)

                        val coin =
                            (searchedCoins.value as RequestState.Success<List<CryptoValue>>).data
                        println("$TAG in searchDatabase and coins are : $coin")
                    }
            }
        } catch (e: Exception) {
            _searchedCoins.value = RequestState.Error(e.localizedMessage as String)
        }
    }


    fun saveSortState(coinSort: CoinSort) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveSortState(coinSort)
            println("$TAG saving sort state : ${coinSort.name}")
        }

    }

    fun getSortState() {
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getSortState().map {
                    CoinSort.valueOf(it)
                }.collect {
                    _sortState.value = RequestState.Success(it)
                    val sortStateCache = (RequestState.Success(it).data)
                    val personCoinsList =
                        (personCoins.value as RequestState.Success<List<CryptoValue>>).data
                    val sortedlist = sortCryptoValueList(personCoinsList, sortStateCache)
                    _personCoins.value = RequestState.Success(sortedlist)
                }
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



