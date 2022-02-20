package com.imtmobileapps.cryptocompose.view.holding

import androidx.activity.compose.BackHandler
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.imtmobileapps.cryptocompose.components.AddHoldingDetailCard
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarContentColor
import com.imtmobileapps.cryptocompose.viewmodel.ManageHoldingsViewModel
import kotlinx.coroutines.flow.collect
import logcat.logcat

@Composable
fun AddHoldingDetailScreen(
    viewModel: ManageHoldingsViewModel,
    onPopBackStack: () -> Unit,
) {

    BackHandler {
        onPopBackStack()
    }

    val TAG = "AddHoldingDetailScreen"

    val selectedCoin = viewModel.selectedCoin.collectAsState()
    val selectedCryptoValue = viewModel.selectedCryptoValue.collectAsState()

    val quantityValueText = rememberSaveable {
        mutableStateOf("")
    }

    val costValueText = rememberSaveable {
        mutableStateOf("")
    }

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

            var coin: Coin? = null
            var crypto: CryptoValue? = null
            selectedCoin.value.let {
                if (it != null) {
                    coin = it
                }

            }
            selectedCryptoValue.value.let {
                if (it != null) {
                    crypto = it
                }
            }

            AddHoldingDetailCard(
                quantityValueText = quantityValueText.value,
                costValueText = costValueText.value,
                selectedCoin = coin,
                selectedCryptoValue = crypto,
                onQuantityChanged = {
                    quantityValueText.value = it
                    logcat(TAG) { "onQuantityChanged and quantity is: $it" }
                },
                onCostChanged = {
                    costValueText.value = it
                    logcat(TAG) { "onCostChanged and cost is: $it" }
                },
                addHoldingClicked = {
                    // TODO call the service
                    logcat(TAG) { "addHoldingClicked! and quantity is : ${quantityValueText.value}" }
                    logcat(TAG) { "addHoldingClicked! and cost is : ${costValueText.value}" }
                }

            )

        }

    )
}