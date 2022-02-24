package com.imtmobileapps.cryptocompose.event

import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue

sealed class ListEvent():UIEvent(){

    data class OnAppInit(val personId: Int) : ListEvent()
    // PersonCoins
    data class OnCoinClicked(val cryptoValue: CryptoValue) : ListEvent()
    // AllCoins
    data class OnAllCoinClicked(val coin: Coin) : ListEvent()

    object OnAddCoinClicked :ListEvent()

    data class OnDeleteCoinClick(val coin: Coin): ListEvent()
    object OnUndoDeleteClick: ListEvent()
}
