package com.imtmobileapps.cryptocompose.view.holding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imtmobileapps.cryptocompose.ui.theme.coinNameTextColor
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarContentColor
import com.imtmobileapps.cryptocompose.viewmodel.ManageHoldingsViewModel

@Composable
fun AddHoldingDetailScreen(
    viewModel: ManageHoldingsViewModel
){

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    //BackAction(onBackClicked = navigateToHoldingListScreen)
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