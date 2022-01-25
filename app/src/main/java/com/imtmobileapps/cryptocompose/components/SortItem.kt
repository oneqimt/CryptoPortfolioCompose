package com.imtmobileapps.cryptocompose.components

import LARGE_PADDING
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.ui.theme.Typography
import com.imtmobileapps.cryptocompose.util.CoinSort

@Composable
fun SortItem(coinSort: CoinSort){

    Row(
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            modifier = Modifier.padding(LARGE_PADDING),
            text = coinSort.name,
            style = Typography.subtitle2,
            color = MaterialTheme.colors.onSurface

        )
    }

}

@Composable
@Preview
fun SortItemPreview(){
    SortItem(coinSort = CoinSort.NAME)
}