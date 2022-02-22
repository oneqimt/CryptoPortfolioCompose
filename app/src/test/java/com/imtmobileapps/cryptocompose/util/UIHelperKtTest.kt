package com.imtmobileapps.cryptocompose.util

import com.google.common.truth.Truth.assertThat
import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue
import junit.framework.TestCase

class UIHelperKtTest : TestCase() {

    private val cryptoValuesList: MutableList<CryptoValue> = mutableListOf()


    public override fun setUp() {
        super.setUp()

        val cryptoValue1 = CryptoValue(
            id = "fasd",
            USD = "yes",
            coin = Coin(
                coinId = 2,
                coinName = "Cardano",
                coinSymbol = "CAR",
                cmcId = 1,
                slug = "btc_btc",
                smallCoinImageUrl = null,
                largeCoinImageUrl = null,
                marketCap = null,
                cmcRank = 1,
                currentPrice = 38000.78.toBigDecimal()),
            holdingValue = "456000",
            percentage = "23456",
            cost = "500",
            increaseDecrease = "2600",
            quantity = 11.9)

        val cryptoValue2 = CryptoValue(
            id = "fasfioew",
            USD = "yes",
            coin = Coin(
                coinId = 3,
                coinName = "Aether",
                coinSymbol = "AETH",
                cmcId = 2,
                slug = "eth_eth",
                smallCoinImageUrl = null,
                largeCoinImageUrl = null,
                marketCap = null,
                cmcRank = 2,
                currentPrice = 2700.78.toBigDecimal()),
            holdingValue = "6500",
            percentage = "363463",
            cost = "100",
            increaseDecrease = "4000",
            quantity = 19.5)

        cryptoValuesList.add(cryptoValue1)
        cryptoValuesList.add(cryptoValue2)


    }

    fun testSortCryptoValueList() {

        val list1 = sortCryptoValueList(cryptoValuesList, CoinSort.NAME)
        val list2 = sortCryptoValueList(cryptoValuesList, CoinSort.SYMBOL)
        val list3 = sortCryptoValueList(cryptoValuesList, CoinSort.NONE)

        assertThat(list1[0].coin.coinName).isEqualTo("Aether")
        assertThat(list2[0].coin.coinSymbol).isEqualTo("AETH")
        assertThat(list3[0].coin.coinName).isEqualTo("Cardano")

    }


    fun testRemoveWhiteSpace() {

        val str = "23   4  4 4"
        val newstr = removeWhiteSpace(str)

        assertThat(newstr).doesNotContain("         ")
        assertThat(newstr).isEqualTo("23444")

    }

    fun testValidateAddHoldingValues() {

        val quantity = "2"
        val cost = "12900.78"

        val valid = validateAddHoldingValues(quantity, cost)

        assertThat(valid).isTrue()

    }
}