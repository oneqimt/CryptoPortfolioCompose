package com.imtmobileapps.cryptocompose.event

import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue

sealed class ListEvent():UiEvent(){

    data class OnAppInit(val personId: Int) : ListEvent()
    data class OnCoinClicked(val cryptoValue: CryptoValue) : ListEvent()
    data class OnListRefresh(val personId: Int): ListEvent()

    data class OnDeleteCoinClick(val coin: Coin): ListEvent()
    object OnUndoDeleteClick: ListEvent()
}
