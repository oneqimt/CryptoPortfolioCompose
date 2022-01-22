package com.imtmobileapps.cryptocompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtmobileapps.cryptocompose.data.CryptoRepositoryImpl
import com.imtmobileapps.cryptocompose.event.ListEvent
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.TotalValues
import com.imtmobileapps.cryptocompose.util.RequestState
import com.imtmobileapps.cryptocompose.util.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val repository: CryptoRepositoryImpl,

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

    // NOTE : personId will come from login layer when it is built
    init {

        fetchCoinsFromRemote(3)
        fetchTotalValuesFromRemote(3)
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
                println("$TAG ListEvent.OnCoinClicked and this cryptoValue is: ${event.cryptoValue.coin.coinName}")
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

    private fun sendUiEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }


    companion object {
        private val TAG = CryptoListViewModel::class.java.simpleName
    }

}



