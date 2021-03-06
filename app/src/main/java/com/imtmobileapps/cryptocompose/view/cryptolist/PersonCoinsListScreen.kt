package com.imtmobileapps.cryptocompose.view.cryptolist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.imtmobileapps.cryptocompose.components.appbar.PersonCoinsListAppBar
import com.imtmobileapps.cryptocompose.event.ListEvent
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.ui.theme.fabBackgroundColor
import com.imtmobileapps.cryptocompose.util.RequestState
import com.imtmobileapps.cryptocompose.util.Routes
import com.imtmobileapps.cryptocompose.viewmodel.CryptoListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import logcat.logcat

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

    LaunchedEffect(key1 = personCoins.value) {
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
                is UIEvent.Logout ->{
                    logcat(TAG){"UIEvent.Logout called"}
                    onNavigate(UIEvent.Navigate(Routes.LOGIN_SCREEN))
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
            it.calculateTopPadding()
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
                                onEvent = viewModel::onEvent,
                                cryptoValue = cryptoValue
                            )

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