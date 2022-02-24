package com.imtmobileapps.cryptocompose.event

sealed class UIEvent {
    object PopBackStack: UIEvent()
    object Logout: UIEvent()
    //object Login: UIEvent()
    data class Navigate(val route: String): UIEvent()
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ): UIEvent()
    data class OnListRefresh(val personId: Int): UIEvent()
}