package com.imtmobileapps.cryptocompose.viewmodel

import androidx.lifecycle.ViewModel
import com.imtmobileapps.cryptocompose.data.CryptoRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: CryptoRepositoryImpl
): ViewModel() {



}