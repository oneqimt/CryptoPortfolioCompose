package com.imtmobileapps.cryptocompose.view.holding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imtmobileapps.cryptocompose.components.CircularProgressBar
import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.util.RequestState
import com.imtmobileapps.cryptocompose.viewmodel.ManageHoldingsViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilteredCoinsView(
    viewModel: ManageHoldingsViewModel
){

    val filteredCoins: State<RequestState<MutableList<Coin>>> =
        viewModel.filteredCoins.collectAsState()

    when(filteredCoins.value){
        RequestState.Loading -> {
            Column(
                modifier = Modifier.padding(30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                CircularProgressBar()
            }
        }

        is RequestState.Success ->{
            val filteredCoinsList =
                (filteredCoins.value as RequestState.Success<MutableList<Coin>>).data.toMutableStateList()
            LazyColumn(
                modifier = Modifier
                    .padding(vertical = 4.dp)
            ) {
                items(filteredCoinsList, key = {
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
    }
}
