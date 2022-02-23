package com.imtmobileapps.cryptocompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtmobileapps.cryptocompose.data.CryptoRepositoryImpl
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.util.RequestState
import com.imtmobileapps.cryptocompose.util.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import logcat.LogPriority
import logcat.logcat
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: CryptoRepositoryImpl
): ViewModel() {

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: UIEvent) {


    }

    fun login(uname: String, pass: String){
        try {
            viewModelScope.launch {
                repository.login(uname, pass).collect {
                    logcat(TAG){"LOGIN SUCCESS COLLECT and signUp is: $it"}
                    val test = RequestState.Success(it).data
                    logcat(TAG){"LOGIN SUCCESS and signUp is: $test"}
                    sendUiEvent(UIEvent.Navigate(Routes.PERSON_COINS_LIST))
                }
            }
        }catch (e: Exception){
            logcat(TAG, LogPriority.ERROR) { e.localizedMessage as String }
        }
    }
    private fun sendUiEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }

}