package com.imtmobileapps.cryptocompose.view.holding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.imtmobileapps.cryptocompose.R
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.ui.theme.*
import com.imtmobileapps.cryptocompose.viewmodel.ManageHoldingsViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun AddHoldingDetailScreen(
    viewModel: ManageHoldingsViewModel,
    onPopBackStack: () -> Unit,
) {

    BackHandler {
        onPopBackStack()
    }

    val selectedCoin = viewModel.selectedCoin.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.Navigate -> onPopBackStack()
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onPopBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                title = {
                    selectedCoin.value?.coinName?.let {
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                elevation = 2.dp,
                border = BorderStroke(0.3.dp, MaterialTheme.colors.cardBorderColor),
                backgroundColor = MaterialTheme.colors.cardBackgroundColor,
                shape = RoundedCornerShape(corner = CornerSize(6.dp))
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp, 20.dp)

                    ) {
                        selectedCoin.value?.coinName?.let { coinName ->
                            Text(
                                modifier = Modifier.padding(2.dp),
                                text = coinName,
                                color = MaterialTheme.colors.coinNameTextColor,
                                style = MaterialTheme.typography.h6,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.padding(10.dp)

                    ) {
                        selectedCoin.value?.coinSymbol?.let { coinSymbol ->
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
                } // end 1st row
                //Spacer(Modifier.height(40.dp))

                Column(modifier = Modifier.padding(10.dp, 70.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp)

                        ) {
                            selectedCoin.value?.currentPrice?.let { currentPrice ->
                                Text(
                                    modifier = Modifier.padding(2.dp),
                                    text = stringResource(id = R.string.current_price_add_holding,
                                        currentPrice.toPlainString()),
                                    color = MaterialTheme.colors.coinNameTextColor,
                                    style = MaterialTheme.typography.h6,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

    )
}