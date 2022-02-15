package com.imtmobileapps.cryptocompose.view.holding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.imtmobileapps.cryptocompose.model.Coin
import com.imtmobileapps.cryptocompose.ui.theme.cardBackgroundColor
import com.imtmobileapps.cryptocompose.ui.theme.cardBorderColor
import com.imtmobileapps.cryptocompose.ui.theme.coinNameTextColor
import com.imtmobileapps.cryptocompose.viewmodel.ManageHoldingsViewModel

@ExperimentalMaterialApi
@Composable
fun AddHoldingListItem(
    coin: Coin
){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        elevation = 2.dp,
        border = BorderStroke(0.3.dp, MaterialTheme.colors.cardBorderColor),
        backgroundColor = MaterialTheme.colors.cardBackgroundColor,
        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
        onClick = {
            //viewModel.onEvent(ListEvent.OnAddCoinClicked(coin))
            println("AddHoldingListItem and coin clicked $coin")
        }
    ) {

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            coin.apply {

                Column(
                    modifier = Modifier
                        .padding(6.dp)

                ) {
                    Text(
                        modifier = Modifier.padding(2.dp),
                        text = cmcRank.toString(),
                        color = MaterialTheme.colors.coinNameTextColor,
                        style = MaterialTheme.typography.subtitle1
                    )
                }

                Column(
                    modifier = Modifier.padding(10.dp))
                {
                    Image(
                        painter = rememberImagePainter(smallCoinImageUrl),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column(modifier = Modifier.padding(2.dp))
                {
                    if (coinName != null) {
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = coinName,
                            color = MaterialTheme.colors.coinNameTextColor,
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.End

                ) {
                    if (coinSymbol != null) {
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = coinSymbol,
                            color = MaterialTheme.colors.coinNameTextColor,
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                }
            }
        }

    }

}