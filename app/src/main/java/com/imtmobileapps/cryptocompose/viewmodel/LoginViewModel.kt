package com.imtmobileapps.cryptocompose.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtmobileapps.cryptocompose.data.CryptoRepositoryImpl
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

    private val _personId: MutableState<Int> = mutableStateOf(0)
    val personId: State<Int> = _personId

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun login(uname: String, pass: String) {
        try {
            viewModelScope.launch {
                repository.login(uname, pass).collect { signUp ->
                    // save the Person object to the database
                    signUp.person.let { person ->
                        // save the personId Int to the dataStore
                        person.personId?.let { repository.savePersonId(it) }
                        savePerson(person)
                    }

                    sendUiEvent(UIEvent.Navigate(Routes.PERSON_COINS_LIST))
                }
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

    private fun sendUiEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }

}