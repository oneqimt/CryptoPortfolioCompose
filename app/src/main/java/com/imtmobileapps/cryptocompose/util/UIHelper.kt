package com.imtmobileapps.cryptocompose.util

import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.TotalValues
import java.math.BigDecimal

enum class DataSource {
    LOCAL,
    REMOTE
}

enum class CoinSort {
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

enum class SearchAppBarState {
    OPENED,
    CLOSED
}

enum class Action {
    ADD,
    UPDATE,
    DELETE,
    DELETE_ALL,
    UNDO,
    NO_ACTION
}

fun sortCryptoValueList(
    list: List<CryptoValue>,
    coinSort: CoinSort,
): List<CryptoValue> {
    when (coinSort) {
        CoinSort.NAME -> {
            return list.sortedBy {
                it.coin.coinName
            }
        }

        CoinSort.SYMBOL -> {
            return list.sortedBy {
                it.coin.coinSymbol
            }
        }

        CoinSort.NONE -> {
            return list
        }
    }

}

fun getDummyCryptoValue(): CryptoValue {

    return CryptoValue(
        id = "",
        USD = "",
        coin = getDummyCoin(1),
        holdingValue = "",
        percentage = "",
        cost = "",
        increaseDecrease = "",
        quantity = 0.0
    )
}

fun getDummyTotalsValue(): TotalValues {
    return TotalValues(
        personId = -1,
        totalCost = "1",
        totalValue = "1",
        totalChange = "1",
        increaseDecrease = ""

    )
}

fun getDummyCoin(index: Int): Coin {

    return Coin(
        0,
        "Dummy",
        "",
        1,
        "",
        "",
        "",
        BigDecimal.valueOf(0.123455),
        index,
        BigDecimal.valueOf(0.123455)
    )
}