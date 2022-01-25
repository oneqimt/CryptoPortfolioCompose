package com.imtmobileapps.cryptocompose.util

import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.TotalValues
import java.math.BigDecimal

enum class CoinSort{
    NAME,
    SYMBOL,
    NONE
}

enum class RowType {
    COIN_NAME,
    COIN_PRICE,
    QUANTITY_HELD,
    HOLDING_VALUE,
    YOUR_COST,
    INCREASE,
    TOTAL_VALUE,
    TOTAL_COST,
    TOTAL_INCREASE

}

enum class SearchAppBarState{
    OPENED,
    CLOSED,
    TRIGGERED
}

enum class Action {
    ADD,
    UPDATE,
    DELETE,
    DELETE_ALL,
    UNDO,
    NO_ACTION

}

fun String?.toAction(): Action {
    return when {
        this == "ADD" -> {
            Action.ADD
        }
        this == "UPDATE" -> {
            Action.UPDATE
        }
        this == "DELETE" -> {
            Action.DELETE
        }
        this == "DELETE_ALL" -> {
            Action.DELETE_ALL
        }
        this == "UNDO" -> {
            Action.UNDO
        }
        else -> {
            Action.NO_ACTION
        }
    }

}

fun getDummyCryptoValue(): CryptoValue {

    return CryptoValue(
        id = "",
        USD = "",
        coin = getDummyCoin(),
        holdingValue = "",
        percentage = "",
        cost = "",
        increaseDecrease = "",
        quantity = 0.0
    )
}

fun getDummyTotalsValue(): TotalValues {
    return TotalValues(
        personId=-1,
        totalCost= "1",
        totalValue="1",
        totalChange="1",
        increaseDecrease= ""

    )
}

fun getDummyCoin(): Coin {

    return Coin(
        0,
        "",
        "",
        0,
        "",
        "",
        "",
        BigDecimal.valueOf(0.123455),
        0,
        BigDecimal.valueOf(0.123455)
    )
}