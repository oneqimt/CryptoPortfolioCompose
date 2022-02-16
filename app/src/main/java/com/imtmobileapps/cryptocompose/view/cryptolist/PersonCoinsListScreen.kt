package com.imtmobileapps.cryptocompose.view.cryptolist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imtmobileapps.cryptocompose.components.CircularProgressBar
import com.imtmobileapps.cryptocompose.event.ListEvent
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.ui.theme.fabBackgroundColor
import com.imtmobileapps.cryptocompose.util.RequestState
import com.imtmobileapps.cryptocompose.util.SearchAppBarState
import com.imtmobileapps.cryptocompose.viewmodel.CryptoListViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun PersonCoinsListScreen(
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: CryptoListViewModel
) {
    val TAG = "PersonCoinsListScreen"
    val personCoins: State<RequestState<List<CryptoValue>>> =
        viewModel.personCoins.collectAsState()

    val scaffoldState = rememberScaffoldState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

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
            PersonCoinsListAppBar(
                viewModel = viewModel
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(ListEvent.OnAddCoinClicked)

            }, backgroundColor = MaterialTheme.colors.fabBackgroundColor) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        content = {

            when (personCoins.value) {
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

                    val list = (personCoins.value as RequestState.Success<List<CryptoValue>>).data

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 4.dp)
                    ) {
                        items(items = list, key = {
                            it.id
                        }) { cryptoValue ->

                            PersonCoinsListItem(
                                //onEvent = viewModel::onEvent,
                                cryptoValue = cryptoValue,
                                viewModel = viewModel)

                        }

                        scope.launch {
                            listState.animateScrollToItem(0, 0)
                        }

                    }

                }

                else -> Unit
            }// end when

        })


}