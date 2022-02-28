package com.imtmobileapps.cryptocompose.util

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.TotalValues
import com.imtmobileapps.cryptocompose.util.Constants.CRYPTO_SENSITIVE_DATA_FILE
import com.imtmobileapps.cryptocompose.util.Constants.MINIMUM_CHARS
import java.io.ByteArrayOutputStream
import java.io.File
import java.math.BigDecimal
import java.nio.charset.StandardCharsets


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

// WRITE to application sandbox
fun writeUsernameAndPassword(context: Context, uname: String, pass: String) {

    val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    val fileToWrite = CRYPTO_SENSITIVE_DATA_FILE

    val storagePath = context.filesDir
    val encryptedFile = EncryptedFile.Builder(
        File(storagePath, fileToWrite),
        context,
        mainKeyAlias,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()
    val str = "$uname:$pass"
    val fileContent = str.toByteArray(StandardCharsets.UTF_8)
    encryptedFile.openFileOutput().apply {
        write(fileContent)
        flush()
        close()
    }
}

// DELETE
fun deleteSensitiveFile(context: Context) :Boolean{
    val fileToDelete = CRYPTO_SENSITIVE_DATA_FILE
    val storagePath = context.filesDir
    val data = File(storagePath,fileToDelete)
    return data.delete()

}

// READ from application sandbox
fun readUsernameAndPassword(context: Context): String {

    val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    val fileToRead = CRYPTO_SENSITIVE_DATA_FILE
    val storagePath = context.filesDir

    val encryptedFile = EncryptedFile.Builder(
        File(storagePath, fileToRead),
        context,
        mainKeyAlias,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()

    val inputStream = encryptedFile.openFileInput()
    val byteArrayOutputStream = ByteArrayOutputStream()
    var nextByte: Int = inputStream.read()
    while (nextByte != -1) {
        byteArrayOutputStream.write(nextByte)
        nextByte = inputStream.read()
    }

    inputStream.apply {
        close()
    }

    return byteArrayOutputStream.toString()

}

fun validateUsername(uname: String): Boolean {
    return uname.isNotEmpty() && uname.length >= MINIMUM_CHARS
}

fun validatePassword(pass: String): Boolean {
    return pass.isNotEmpty() && pass.length >= MINIMUM_CHARS
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