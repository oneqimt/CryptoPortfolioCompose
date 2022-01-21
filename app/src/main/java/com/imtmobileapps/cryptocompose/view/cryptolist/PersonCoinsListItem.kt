package com.imtmobileapps.cryptocompose.view.cryptolist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.imtmobileapps.cryptocompose.event.ListEvent
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.viewmodel.CryptoListViewModel

@Composable
fun PersonCoinsListItem(
    onEvent: (ListEvent) -> Unit,
    cryptoValue: CryptoValue,
    modifier: Modifier = Modifier,
    viewModel: CryptoListViewModel
) {

}