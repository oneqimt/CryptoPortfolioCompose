package com.imtmobileapps.cryptocompose.util

import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.TotalValues
import java.math.BigDecimal

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