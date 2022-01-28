package com.imtmobileapps.cryptocompose.view.holding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.imtmobileapps.cryptocompose.R
import com.imtmobileapps.cryptocompose.components.CircularProgressBar
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.ui.theme.coinNameTextColor
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarContentColor
import com.imtmobileapps.cryptocompose.util.Action
import com.imtmobileapps.cryptocompose.util.RequestState
import com.imtmobileapps.cryptocompose.util.Routes
import com.imtmobileapps.cryptocompose.view.cryptolist.BackAction
import com.imtmobileapps.cryptocompose.view.cryptolist.PersonCoinsListItem
import com.imtmobileapps.cryptocompose.viewmodel.ManageHoldingsViewModel

@ExperimentalMaterialApi
@Composable
fun AddHoldingListScreen(
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: ManageHoldingsViewModel,
) {

    val TAG = "AddHoldingListScreen"
    val allCoins: State<RequestState<List<Coin>>> =
        viewModel.allCoins.collectAsState()

    val route = Routes.PERSON_COINS_LIST
    // System back button
    BackHandler {
        onNavigate(UIEvent.Navigate(route))
    }

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,

        topBar = {
            TopAppBar(
                elevation = 4.dp,
                navigationIcon = {

                    IconButton(onClick = { onNavigate(UIEvent.Navigate(route)) }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                title = {
                    Text(
                        text = stringResource(id = R.string.add_holding),
                        color = MaterialTheme.colors.topAppBarContentColor
                    )
                },
                actions = {

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = {/* Do Something*/ }) {
                        Icon(Icons.Filled.Settings, "Settings")
                    }
                },
                backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor

            )
        },

        content = {
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

                    val list = (allCoins.value as RequestState.Success<List<Coin>>).data

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 4.dp)
                    ) {
                        items(items = list, key = { coin ->
                            coin.cmcRank as Int
                        }) { coin ->
                            AddHoldingListItem(coin = coin, viewModel = viewModel)
                        }
                    }
                }

                else -> Unit
            }// end when
        }

    )
}

