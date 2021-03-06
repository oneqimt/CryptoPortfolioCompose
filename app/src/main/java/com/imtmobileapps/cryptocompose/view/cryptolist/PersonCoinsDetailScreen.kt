package com.imtmobileapps.cryptocompose.view.cryptolist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.imtmobileapps.cryptocompose.R
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.model.TotalValues
import com.imtmobileapps.cryptocompose.ui.theme.*
import com.imtmobileapps.cryptocompose.util.RequestState
import com.imtmobileapps.cryptocompose.util.RowType
import com.imtmobileapps.cryptocompose.viewmodel.CryptoListViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun PersonCoinsDetailScreen(
    onPopBackStack: () -> Unit,
    viewModel: CryptoListViewModel
) {

    BackHandler {
        onPopBackStack()
    }
    // remember calculates the value passed to it only during the first composition. It then
    // returns the same value for every subsequent composition.
    val scrollState = rememberScrollState()
    val scaffoldState =
        rememberScaffoldState() // This is here in case we want to display a snackbar

    // this is not wrapped because it is local
    val selectedCryptoValue: State<CryptoValue?> = viewModel.selectedCryptoValue.collectAsState()

    // pull the TotalValues object out of the RequestState wrapper
    val totalValuesFromModel: State<RequestState<TotalValues?>> =
        viewModel.totalValues.collectAsState()

    // we need to get the person's TotalValues from database as well
    val success = totalValuesFromModel.value as RequestState.Success<*>
    val totalValues = success.data as TotalValues?

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.PopBackStack -> onPopBackStack()

                else -> Unit
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,

        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {onPopBackStack()}) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                title = {
                    selectedCryptoValue.value?.coin?.coinName?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colors.topAppBarContentColor
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
            )
        },

        content = {
            LazyColumn(
                modifier = Modifier
                    .scrollable(
                        state = scrollState,
                        orientation = Orientation.Vertical
                    )
                    .padding(10.dp, 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)

            ) {

                item {
                    Card(
                        elevation = 6.dp,
                        border = BorderStroke(0.3.dp, MaterialTheme.colors.cardBorderColor),
                        backgroundColor = MaterialTheme.colors.cardBackgroundColor,
                        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)

                    ) {

                        Column(
                            modifier = Modifier.padding(10.dp, 10.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            PersonCoinsDetailRow(
                                cryptoValue = selectedCryptoValue.value,
                                null,
                                RowType.COIN_NAME)

                            PersonCoinsDetailRow(
                                cryptoValue = selectedCryptoValue.value,
                                null,
                                rowType = RowType.COIN_PRICE)

                            PersonCoinsDetailRow(
                                cryptoValue = selectedCryptoValue.value,
                                null,
                                rowType = RowType.QUANTITY_HELD)

                            PersonCoinsDetailRow(
                                cryptoValue = selectedCryptoValue.value,
                                null,
                                rowType = RowType.HOLDING_VALUE)

                            PersonCoinsDetailRow(
                                cryptoValue = selectedCryptoValue.value,
                                null,
                                rowType = RowType.YOUR_COST)

                            PersonCoinsDetailRow(
                                cryptoValue = selectedCryptoValue.value,
                                null,
                                rowType = RowType.INCREASE)

                            Spacer(modifier = Modifier.height(6.dp))

                        }

                    }// card end
                }

                item {
                    Card(
                        elevation = 6.dp,
                        border = BorderStroke(0.3.dp, MaterialTheme.colors.cardBorderColor),
                        backgroundColor = MaterialTheme.colors.cardBackgroundColor,
                        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)

                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp, 10.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp, 5.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    modifier = Modifier.padding(2.dp),
                                    text = stringResource(id = R.string.total_portfolio_holdings),
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colors.coinNameTextColor,
                                    style = MaterialTheme.typography.subtitle1,
                                )
                            }

                            PersonCoinsDetailRow(
                                cryptoValue = null,
                                totalValues,
                                rowType = RowType.TOTAL_VALUE)

                            PersonCoinsDetailRow(
                                cryptoValue = null,
                                totalValues,
                                rowType = RowType.TOTAL_COST)

                            PersonCoinsDetailRow(
                                cryptoValue = null,
                                totalValues,
                                rowType = RowType.TOTAL_INCREASE)

                            Spacer(modifier = Modifier.height(6.dp))

                        }
                    }// 2nd card end
                }// item 2 end

                item {
                    Card(
                        elevation = 6.dp,
                        border = BorderStroke(0.3.dp, MaterialTheme.colors.cardBorderColor),
                        backgroundColor = MaterialTheme.colors.cardBackgroundColor,
                        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)

                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp, 10.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp, 5.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {

                                val newsTitle =
                                    selectedCryptoValue.value?.coin?.coinName.toString() + " " + "NEWS"
                                Text(
                                    modifier = Modifier.padding(2.dp),
                                    text = newsTitle,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colors.coinNameTextColor,
                                    style = MaterialTheme.typography.subtitle1,
                                )
                            }
                        }
                    }// third card end
                } // item 3 end

            }// main column end
        }// content end
    ) // scaffold end


}
