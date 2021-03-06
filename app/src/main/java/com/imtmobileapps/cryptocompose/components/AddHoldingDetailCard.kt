package com.imtmobileapps.cryptocompose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imtmobileapps.cryptocompose.R
import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.ui.theme.cardBackgroundColor
import com.imtmobileapps.cryptocompose.ui.theme.cardBorderColor
import com.imtmobileapps.cryptocompose.ui.theme.coinNameTextColor
import com.imtmobileapps.cryptocompose.util.getDummyCoin
import com.imtmobileapps.cryptocompose.util.getDummyCryptoValue
import logcat.logcat

@Composable
fun AddHoldingDetailCard(
    quantityValueText: String,
    costValueText: String,
    selectedCoin: Coin?,
    selectedCryptoValue: CryptoValue?,
    onQuantityChanged: (String) -> Unit,
    onCostChanged: (String) -> Unit,
    addHoldingClicked: () -> Unit,
    onDone: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp),
        elevation = 4.dp,
        border = BorderStroke(0.3.dp, MaterialTheme.colors.cardBorderColor),
        backgroundColor = MaterialTheme.colors.cardBackgroundColor,
        shape = RoundedCornerShape(corner = CornerSize(6.dp))
    ) {

        val focusManager = LocalFocusManager.current
        val TAG = "AddHoldingDetailCard"

        Column(modifier =
        Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState()))
        {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (selectedCoin != null) {
                    selectedCoin.coinName?.let { coinName ->
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = coinName,
                            color = MaterialTheme.colors.coinNameTextColor,
                            style = MaterialTheme.typography.h6,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    selectedCoin.coinSymbol?.let { coinSymbol ->
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = coinSymbol,
                            color = MaterialTheme.colors.coinNameTextColor,
                            style = MaterialTheme.typography.h6,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }// 1st row

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (selectedCoin != null) {
                    selectedCoin.currentPrice?.let { currentPrice ->
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = stringResource(id = R.string.current_price_add_holding,
                                currentPrice.toPlainString()),
                            color = MaterialTheme.colors.coinNameTextColor,
                            style = MaterialTheme.typography.subtitle1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }// end 2nd row
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                selectedCryptoValue?.coin?.coinName?.let {
                    Text(modifier = Modifier.padding(2.dp),
                        text = stringResource(id = R.string.you_have, it),
                        color = MaterialTheme.colors.coinNameTextColor,
                        style = MaterialTheme.typography.subtitle2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }// end 3rd row

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                selectedCryptoValue?.quantity?.let {
                    Text(modifier = Modifier.padding(2.dp),
                        text = stringResource(id = R.string.quantity_held_add_holding,
                            it.toString()),
                        color = MaterialTheme.colors.coinNameTextColor,
                        style = MaterialTheme.typography.subtitle2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }// end 4th row

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                selectedCryptoValue?.holdingValue?.let {
                    Text(modifier = Modifier.padding(2.dp),
                        text = stringResource(id = R.string.holding_value_add_holding, it),
                        color = MaterialTheme.colors.coinNameTextColor,
                        style = MaterialTheme.typography.subtitle2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                }

            }// end 5th row

            Spacer(modifier = Modifier.height(30.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(

                    value = quantityValueText,
                    label = { Text(text = stringResource(id = R.string.quantity)) },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),

                    onValueChange = {
                        onQuantityChanged(it)
                    },
                )
            }// end 6th row

            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                TextField(
                    value = costValueText,
                    label = { Text(text = stringResource(id = R.string.cost_per_coin)) },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        // TODO add validation
                        logcat(TAG){" quantity is $quantityValueText cost is : $costValueText"}
                        onDone()
                    }),

                    onValueChange = {
                        onCostChanged(it)
                    }
                )
            }// end 7th row

            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    // TODO add validation
                    logcat(TAG){" quantity is $quantityValueText cost is : $costValueText"}
                    addHoldingClicked()
                }, modifier = Modifier.padding(8.dp)) {
                    Text(text = stringResource(id = R.string.add_holding))
                }
            }// end 8th row

        }// end Column

    }// end card

}

@Preview
@Composable
fun AddHoldingDetailCardPreview() {
    val quantityValueText = "2"
    val costValueText = "1.236"
    val selectedCoin: Coin = getDummyCoin(0)
    val selectedCryptoValue: CryptoValue = getDummyCryptoValue()

    AddHoldingDetailCard(
        quantityValueText = quantityValueText,
        costValueText = costValueText,
        selectedCoin,
        selectedCryptoValue,
        onQuantityChanged = {},
        onCostChanged = {},
        addHoldingClicked = {},
        onDone = {}
    )

}