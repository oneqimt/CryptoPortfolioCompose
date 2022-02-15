package com.imtmobileapps.cryptocompose.view.holding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.imtmobileapps.cryptocompose.components.CircularProgressBar
import com.imtmobileapps.cryptocompose.event.ListEvent
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.util.RequestState
import com.imtmobileapps.cryptocompose.util.Routes
import com.imtmobileapps.cryptocompose.view.appbar.SearchViewActionBar
import com.imtmobileapps.cryptocompose.viewmodel.ManageHoldingsViewModel
import kotlinx.coroutines.flow.collect


@ExperimentalMaterialApi
@Composable
fun AddHoldingListScreen(
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: ManageHoldingsViewModel,
) {

    val TAG = "AddHoldingListScreen"
    val allCoins: State<RequestState<MutableList<Coin>>> =
        viewModel.allCoins.collectAsState()

    // System back button
    BackHandler {
        onNavigate(UIEvent.Navigate(Routes.PERSON_COINS_LIST))
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
                is UIEvent.Navigate -> {
                    println("$TAG and UIEvent.Navigate called and route is ${event.route}")
                    onNavigate(event)
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,

        topBar = {
            SearchViewActionBar(viewModel = viewModel)
        }

    ) {
        val view = LocalView.current
        val lazyListState = rememberLazyListState()
        var text by remember { mutableStateOf("") }

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

                LazyColumn(
                    // state = lazyListState,
                    modifier = Modifier
                        //.fillMaxSize()
                        .padding(vertical = 4.dp)
                ) {

                    val list = (allCoins.value as RequestState.Success<MutableList<Coin>>).data
                    items(list, key = {
                        it.cmcRank
                    }) {
                        AddHoldingListItem(coin = it)
                    }
                }
            }
            else -> {}
        }
    }
}


