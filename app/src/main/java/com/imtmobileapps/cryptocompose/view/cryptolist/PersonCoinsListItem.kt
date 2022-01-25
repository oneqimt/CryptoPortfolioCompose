package com.imtmobileapps.cryptocompose.view.cryptolist

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.imtmobileapps.cryptocompose.event.ListEvent
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.ui.theme.*
import com.imtmobileapps.cryptocompose.viewmodel.CryptoListViewModel

@ExperimentalMaterialApi
@Composable
fun PersonCoinsListItem(
    onEvent: (ListEvent) -> Unit,
    cryptoValue: CryptoValue,
    modifier: Modifier = Modifier,
    viewModel: CryptoListViewModel
) {

    Card(
        modifier = modifier,
        elevation = 2.dp,
        border = BorderStroke(0.3.dp, MaterialTheme.colors.cardBorderColor),
        backgroundColor = MaterialTheme.colors.cardBackgroundColor,
        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
        onClick = {
            //println("PersonCoinsListItem and cryptoValue is : ${cryptoValue.id}")
            viewModel.onEvent(ListEvent.OnCoinClicked(cryptoValue))
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            cryptoValue.coin.apply {

                Column(
                    modifier = Modifier.padding(10.dp)

                ) {
                    Image(
                        painter = rememberImagePainter(smallCoinImageUrl),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(6.dp)

                ) {
                    if (coinName != null) {
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = coinName,
                            color = MaterialTheme.colors.coinNameTextColor,
                            style = MaterialTheme.typography.subtitle1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (coinSymbol != null) {
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = coinSymbol,
                            color = MaterialTheme.colors.coinSymbolTextColor,
                            style = MaterialTheme.typography.subtitle2,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                }

            }

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.End

            ) {
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = "Current Price:",
                    color = MaterialTheme.colors.staticTextColor,
                    style = MaterialTheme.typography.caption
                )

                cryptoValue.coin.apply {
                    Text(
                        modifier = Modifier.padding(2.dp),
                        text = currentPrice.toString(),
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Red,
                        style = MaterialTheme.typography.caption,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        modifier = Modifier.padding(2.dp),
                        text = "Holding Value:",
                        color = MaterialTheme.colors.staticTextColor,
                        style = MaterialTheme.typography.caption
                    )

                    cryptoValue.apply {
                        Text(
                            modifier = Modifier.padding(2.dp),
                            text = holdingValue,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.staticTextColor,
                            style = MaterialTheme.typography.caption,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }//end

}