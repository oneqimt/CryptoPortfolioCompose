package com.imtmobileapps.cryptocompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imtmobileapps.cryptocompose.data.CryptoApiState
import com.imtmobileapps.cryptocompose.data.CryptoRepositoryImpl
import com.imtmobileapps.cryptocompose.model.CryptoValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val repository: CryptoRepositoryImpl

) : ViewModel() {

    private val _personCoins =
        MutableStateFlow<CryptoApiState<List<CryptoValue>>>(CryptoApiState.loading())
    val personCoins: StateFlow<CryptoApiState<List<CryptoValue>>> = _personCoins

    init {
        // calls can be made here if necessary
       getPersonCoins(3)
    }


    fun getPersonCoins(personId: Int) {

        _personCoins.value = CryptoApiState.loading()

        viewModelScope.launch() {
            repository.getPersonCoins(personId).catch {
                _personCoins.value =
                    CryptoApiState.error(it.message.toString())

            }.collect {
                _personCoins.value = CryptoApiState.success(it.data)

            }
        }
    }

    companion object {
        private val TAG = CryptoListViewModel::class.java.simpleName
    }

}



