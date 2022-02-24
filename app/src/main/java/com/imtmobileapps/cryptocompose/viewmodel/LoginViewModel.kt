package com.imtmobileapps.cryptocompose.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtmobileapps.cryptocompose.data.CryptoRepositoryImpl
import com.imtmobileapps.cryptocompose.event.ListEvent
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.model.Person
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
    private val repository: CryptoRepositoryImpl,
) : ViewModel() {

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()





    private fun sendUiEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }

}