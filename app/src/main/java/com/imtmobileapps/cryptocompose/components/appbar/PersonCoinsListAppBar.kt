package com.imtmobileapps.cryptocompose.components.appbar

import LARGE_PADDING
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.imtmobileapps.cryptocompose.R
import com.imtmobileapps.cryptocompose.components.SortItem
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarContentColor
import com.imtmobileapps.cryptocompose.util.CoinSort
import com.imtmobileapps.cryptocompose.util.deleteSensitiveFile
import com.imtmobileapps.cryptocompose.viewmodel.CryptoListViewModel
import kotlinx.coroutines.launch
import logcat.logcat


@Composable
fun PersonCoinsListAppBar(
    viewModel: CryptoListViewModel,
) {

    val TAG = "PersonCoinsListAppBar"
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    DefaultListAppBar(
        onSortClicked = {
            println("PersonCoinsListAppBar and init sort state is: ${it.name}")
            viewModel.saveSortState(it)
            // a call to this will set the value
            viewModel.getSortState()
            println("PersonCoinsListAppBar and sort state on viewmodel is : ${viewModel.sortState.value}")
        },
        onDeleteAllConfirmed = {
            logcat(TAG) { "onDeleteAllConfirmed clicked" }
        },
        onLogoutClicked = {
            logcat(TAG) { "onLogout clicked" }
            // delete file here, because Context object is needed
            scope.launch {
                try {
                    deleteSensitiveFile(context = context)
                } catch (e: Exception) {
                    logcat(TAG) { "Problem DELETING FILE ${e.localizedMessage as String}" }
                }

            }
            viewModel.logout()

        },
        onSettingsClicked = {
            logcat(TAG) { "onSettings clicked" }
        }

    )

}

@Composable
fun DefaultListAppBar(
    onSortClicked: (CoinSort) -> Unit,
    onDeleteAllConfirmed: () -> Unit,
    onLogoutClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
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
                onDeleteAllConfirmed = onDeleteAllConfirmed,
                onLogoutClicked = onLogoutClicked,
                onSettingsClicked = onSettingsClicked
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )

}


@Composable
fun ListAppBarActions(
    onSortClicked: (CoinSort) -> Unit,
    onDeleteAllConfirmed: () -> Unit,
    onLogoutClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
) {

    SortAction(onSortClicked = onSortClicked)
    VerticalMenuAction(
        onDeleteAllConfirmed = {
            onDeleteAllConfirmed()
        },
        onSettingsClicked = {
            onSettingsClicked()
        },
        onLogoutClicked = {
            onLogoutClicked()
        }
    )

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
fun VerticalMenuAction(
    onDeleteAllConfirmed: () -> Unit,
    onLogoutClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = {
        expanded = true
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_vertical_menu),
            contentDescription = "vertical menu",
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

            DropdownMenuItem(onClick = {
                expanded = false
                onSettingsClicked()
            }) {
                Text(
                    modifier = Modifier.padding(start = LARGE_PADDING),
                    text = stringResource(R.string.settings),
                    style = MaterialTheme.typography.subtitle2
                )
            }

            DropdownMenuItem(onClick = {
                expanded = false
                onLogoutClicked()

            }) {
                Text(
                    modifier = Modifier.padding(start = LARGE_PADDING),
                    text = stringResource(R.string.logout),
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
        onDeleteAllConfirmed = {},
        onLogoutClicked = {},
        onSettingsClicked = {}
    )
}
