package com.imtmobileapps.cryptocompose.view.cryptolist

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.imtmobileapps.cryptocompose.model.CryptoValue
import com.imtmobileapps.cryptocompose.util.Action
import com.imtmobileapps.cryptocompose.util.SearchAppBarState
import com.imtmobileapps.cryptocompose.viewmodel.CryptoListViewModel


@Composable
fun PersonCoinsListAppBar(
    viewModel: CryptoListViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
){

    when(searchAppBarState){
        SearchAppBarState.CLOSED ->{
            DefaultListAppBar(
                onSearchClicked = {
                    viewModel.searchAppBarState.value = SearchAppBarState.OPENED
                },
                onSortClicked = {
                    viewModel.persistSortState(it)},
                onDeleteAllConfirmed = {
                    viewModel.action.value = Action.DELETE_ALL
                }
            )
        }
        else ->{

            SearchAppBar(
                text = searchTextState,
                onTextChange = { newText ->
                    viewModel.searchTextState.value = newText
                },
                onCloseClicked = {
                    viewModel.searchAppBarState.value = SearchAppBarState.CLOSED
                    viewModel.searchTextState.value = ""
                },
                onSearchClicked = {
                    viewModel.searchDatabase(searchQuery = it)
                }
            )

        }
    }

}

@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (CryptoValue) -> Unit,
    onDeleteAllConfirmed: () -> Unit
){

}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
){

}

@Composable
@Preview
private fun DefaultListAppBarPreview() {
    DefaultListAppBar(
        onSearchClicked = {},
        onSortClicked = {},
        onDeleteAllConfirmed = {}
    )
}

@Composable
@Preview
private fun SearchAppBarPreview() {
    SearchAppBar(
        text = "",
        onTextChange = {},
        onCloseClicked = {},
        onSearchClicked = {}
    )
}