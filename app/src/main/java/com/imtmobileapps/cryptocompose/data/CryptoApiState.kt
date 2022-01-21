package com.imtmobileapps.cryptocompose.data

data class CryptoApiState<out T>(val status: Status, val data: T?, val message: String?) {


    companion object {

        fun <T> success(data: T?): CryptoApiState<T> {
            return CryptoApiState(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String): CryptoApiState<T> {
            return CryptoApiState(Status.ERROR, null, msg)
        }

        fun <T> loading(): CryptoApiState<T> {
            return CryptoApiState(Status.LOADING, null, null)
        }

    }

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}
