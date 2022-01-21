package com.imtmobileapps.cryptocompose.viewmodel

import androidx.lifecycle.ViewModel
import com.imtmobileapps.cryptocompose.data.CryptoRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CryptoListViewModel @Inject constructor(
    private val repository: CryptoRepositoryImpl

) : ViewModel() {


    init {
        // calls can be made here if necessary//
       getPersonCoins(3)
    }


    fun getPersonCoins(personId: Int) {

    }

    companion object {
        private val TAG = CryptoListViewModel::class.java.simpleName
    }

}



