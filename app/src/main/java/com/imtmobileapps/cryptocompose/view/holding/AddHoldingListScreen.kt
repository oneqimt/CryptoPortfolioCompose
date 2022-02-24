package com.imtmobileapps.cryptocompose.view.holding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imtmobileapps.cryptocompose.components.CircularProgressBar
import com.imtmobileapps.cryptocompose.components.appbar.SearchViewActionBar
import com.imtmobileapps.cryptocompose.event.ListEvent
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.util.DataType
import com.imtmobileapps.cryptocompose.util.RequestState
import com.imtmobileapps.cryptocompose.viewmodel.ManageHoldingsViewModel
import kotlinx.coroutines.flow.collect


@ExperimentalMaterialApi
@Composable
fun AddHoldingListScreen(
    onNavigate: () -> Unit,
    onPopBackStack: () -> Unit,
    viewModel: ManageHoldingsViewModel
) {

    val allCoins: State<RequestState<MutableList<Coin>>> =
        viewModel.allCoins.collectAsState()

    val dataType by viewModel.dataType

    // System back button
    BackHandler {
        onPopBackStack()
    }

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(ListEvent.OnUndoDeleteClick)
                    }
                }
                is UIEvent.PopBackStack -> {
                    onPopBackStack()
                }

                is UIEvent.Navigate ->{
                    onNavigate()
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,

        topBar = {
            SearchViewActionBar(viewModel = viewModel, onPopBackStack = onPopBackStack)
        }

    ) {

        when (dataType) {
            DataType.ALL_COINS -> {
                when (allCoins.value) {
                    RequestState.Loading -> {
                        Column(
                            modifier = Modifier.padding(30.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            CircularProgressBar()
                        }
                    }

                    is RequestState.Success -> {

                        val allCoinsList =
                            (allCoins.value as RequestState.Success<MutableList<Coin>>).data

                        LazyColumn(
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                        ) {

                            items(allCoinsList, key = {
                                it.cmcRank
                            }) {
                                AddHoldingListItem(
                                    coin = it,
                                    onEvent = viewModel::onEvent
                                )
                            }

                        }
                    }
                    else -> {}
                }// end 1st when
            }

            DataType.FILTERED_COINS -> {
                FilteredCoinsView(viewModel = viewModel)
            }
        }


    }
}


