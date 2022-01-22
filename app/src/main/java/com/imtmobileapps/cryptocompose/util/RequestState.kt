package com.imtmobileapps.cryptocompose.util

sealed class RequestState<out T>{

    object Idle: RequestState<Nothing>()
    object Loading: RequestState<Nothing>()
    data class Success<T>(val data: T): RequestState<T>()
    data class Error(val error: String): RequestState<Nothing>()

}
