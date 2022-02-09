package com.imtmobileapps.cryptocompose.util

import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.TotalValues
import java.math.BigDecimal
import java.util.*

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

/*val countries = listOf("Germany", "India", "Japan", "Brazil", "Australia")
val filterList = countries.filter { it.length > 5 }
assertEquals(3, list.size)
assertTrue(list.containsAll(listOf("Germany","Brazil","Australia")))*/

// SEARCH in AddHoldingListScreen
fun sortAllCoinsList(searchText: String, originalList: MutableList<Coin>): List<Coin> {

   // val returnList = mutableListOf<Coin>()
    val textValueToFilterOn = searchText.lowercase(Locale.getDefault())

    //val over20 = people.filter { it.age > 20 }

    val filteredList = originalList.filter {
        it.coinName == textValueToFilterOn || it.coinSymbol == textValueToFilterOn
    }

    println("sortAllCoinsList and filteredList is: $filteredList")

    return filteredList

}

/*val copyList = arrayListOf<Coin>()
copyList.addAll(originalList)

// TODO may be the problem here change original list to mutable list
val mlist = originalList as MutableList

mlist.clear()
if (searchText.isEmpty()) {
    mlist.addAll(copyList)
} else {
    val mytext = searchText.lowercase(Locale.getDefault())
    for (coin in copyList) {
        val coinName = coin.coinName?.lowercase(Locale.getDefault()) ?: ""
        val coinSymbol = coin.coinSymbol?.lowercase(Locale.getDefault()) ?: ""
        if (coinName.contains(mytext) || coinSymbol.contains(mytext)) {
            if (!mlist.contains(coin)) {
                mlist.add(coin)
            }
        }
    }
}*/

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