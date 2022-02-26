package com.imtmobileapps.cryptocompose.util

import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.ReturnDTO
import com.imtmobileapps.cryptocompose.model.TotalValues
import java.math.BigDecimal
import kotlin.math.cos

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

// This would be a good extension for String class
fun removeWhiteSpace(str: String): String {

    if (str.isNotEmpty()) {
        return str.filter {
            !it.isWhitespace()
        }
    } else {
        return ""
    }

}

fun validateUsername(uname: String):Boolean{

    return uname.isNotEmpty()
}

fun validatePassword(pass: String):Boolean{
    return pass.isNotEmpty()
}


fun validateAddHoldingValues(quantity: String, cost: String): Boolean {
    val quantityClean = removeWhiteSpace(quantity)
    val costClean = removeWhiteSpace(cost)
    return quantityClean.isNotEmpty() && costClean.isNotEmpty()
}

fun getDummyCryptoValue(): CryptoValue {

    return CryptoValue(
        id = "",
        USD = "",
        coin = getDummyCoin(1),
        holdingValue = "testing",
        percentage = "percentage",
        cost = "cost",
        increaseDecrease = "inc/de",
        quantity = 0.12
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
        "DUM",
        1,
        "slug",
        "imageurl",
        "largeimageurl",
        BigDecimal.valueOf(0.123455),
        index,
        BigDecimal.valueOf(0.123455)
    )
}