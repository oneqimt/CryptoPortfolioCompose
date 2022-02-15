package com.imtmobileapps.cryptocompose.view.cryptolist

import LARGE_PADDING
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.imtmobileapps.cryptocompose.R
import com.imtmobileapps.cryptocompose.components.SortItem
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarContentColor
import com.imtmobileapps.cryptocompose.util.CoinSort
import com.imtmobileapps.cryptocompose.view.appbar.SearchAppBar
import com.imtmobileapps.cryptocompose.viewmodel.CryptoListViewModel


@Composable
fun PersonCoinsListAppBar(
    viewModel: CryptoListViewModel
) {

    DefaultListAppBar(
        onSortClicked = {
            println("PersonCoinsListAppBar and init sort state is: ${it.name}")
            viewModel.saveSortState(it)
            // a call to this will set the value
            viewModel.getSortState()
            println("PersonCoinsListAppBar and sort state on viewmodel is : ${viewModel.sortState.value}")
        },
        onDeleteAllConfirmed = {
            //viewModel.action.value = Action.DELETE_ALL
        }
    )

}

@Composable
fun DefaultListAppBar(
    onSortClicked: (CoinSort) -> Unit,
    onDeleteAllConfirmed: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.coins),
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        actions = {
            ListAppBarActions(
                onSortClicked = onSortClicked,
                onDeleteAllConfirmed = onDeleteAllConfirmed
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )

}


@Composable
fun ListAppBarActions(
    onSortClicked: (CoinSort) -> Unit,
    onDeleteAllConfirmed: () -> Unit,
) {
    var openDialog by remember {
        mutableStateOf(false)
    }

    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(onDeleteAllConfirmed = { openDialog = true })

}

@Composable
fun SortAction(
    onSortClicked: (CoinSort) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = {
        expanded = true
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = stringResource(id = R.string.sort_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSortClicked(CoinSort.NAME)
                })
            {
                SortItem(coinSort = CoinSort.NAME)
            }
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSortClicked(CoinSort.SYMBOL)
                })
            {
                SortItem(coinSort = CoinSort.SYMBOL)
            }
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onSortClicked(CoinSort.NONE)
                })
            {
                SortItem(coinSort = CoinSort.NONE)
            }
        }

    }

}

@Composable
fun DeleteAllAction(
    onDeleteAllConfirmed: () -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = {
        expanded = true
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_vertical_menu),
            contentDescription = stringResource(id = R.string.delete_all_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                onDeleteAllConfirmed()
            }
            ) {

                Text(
                    modifier = Modifier.padding(start = LARGE_PADDING),
                    text = stringResource(R.string.delete_all_action),
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}

@Composable
@Preview
private fun DefaultListAppBarPreview() {
    DefaultListAppBar(
        onSortClicked = {},
        onDeleteAllConfirmed = {}
    )
}
