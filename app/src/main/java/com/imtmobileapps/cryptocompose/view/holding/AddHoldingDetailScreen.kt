package com.imtmobileapps.cryptocompose.view.holding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.ui.theme.coinNameTextColor
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarContentColor
import com.imtmobileapps.cryptocompose.viewmodel.ManageHoldingsViewModel
import kotlinx.coroutines.flow.collect

@Composable
fun AddHoldingDetailScreen(
    viewModel: ManageHoldingsViewModel,
    onPopBackStack: () -> Unit
) {

    BackHandler {
        onPopBackStack()
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
                    IconButton(onClick = {onPopBackStack()}) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                title = {
                    Text(
                        text = "ADD NAME OF COIN",
                        color = MaterialTheme.colors.topAppBarContentColor
                    )
                },
                backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor

            )
        },

        content = {
            Column(
                modifier = Modifier.padding(30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                //CircularProgressBar()
                Text(
                    modifier = Modifier.padding(10.dp, 10.dp),
                    text = "ADD HOLDING DETAIL VIEW",
                    color = MaterialTheme.colors.coinNameTextColor,
                    style = MaterialTheme.typography.subtitle1,
                )
            }
        }

    )

}