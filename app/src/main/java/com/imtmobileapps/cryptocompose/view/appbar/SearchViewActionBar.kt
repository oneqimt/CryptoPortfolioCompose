package com.imtmobileapps.cryptocompose.view.appbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.imtmobileapps.cryptocompose.R
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarContentColor
import com.imtmobileapps.cryptocompose.util.Routes
import com.imtmobileapps.cryptocompose.util.SearchAppBarState
import com.imtmobileapps.cryptocompose.viewmodel.ManageHoldingsViewModel
import logcat.LogPriority
import logcat.logcat

@Composable
fun SearchViewActionBar(
    viewModel: ManageHoldingsViewModel
){
    val searchState by viewModel.searchState
    val searchTextState by viewModel.searchTextState
    val TAG = "SearchViewActionBar"

            AppBar(
                searchState = searchState,
                searchTextState = searchTextState,
                onTextChange = {
                    viewModel.updateSearchTextState(it)
                    viewModel.filterListForSearch()
                    logcat(TAG) { "onTextChange and it is : $it" }
                },
                onCloseClicked = {
                    viewModel.updateSearchState(SearchAppBarState.CLOSED)
                    logcat(TAG) { "onCloseClicked" }
                    viewModel.fetchAllCoinsFromRemote()

                },
                onSearchClicked = {
                    logcat(TAG) { "onSearchClicked" }
                },
                onSearchTriggered = {
                    viewModel.updateSearchState(SearchAppBarState.OPENED)
                    logcat(TAG) { "onSearchTriggered" }
                },
                onNavigate = {
                    logcat(TAG) { "AppBar onNavigate called" }
                    viewModel.onEvent(UIEvent.Navigate(it.route))
                }
            )
}

@Composable
fun DefaultAppBar(
    onSearchClicked: () -> Unit,
    onNavigate: (UIEvent.Navigate) -> Unit
){
    TopAppBar(
        elevation = 4.dp,
        navigationIcon = {

            IconButton(onClick = {
                logcat("DefaultAppBar") { "DefaultAppBar onNavigate called" }
                onNavigate(UIEvent.Navigate(Routes.PERSON_COINS_LIST))
            }
            ) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.add_holding),
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },

        actions = {
            IconButton(onClick = {
                onSearchClicked()
                logcat("DefaultAppBar") { "Search Clicked" }
            }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.search_coins),
                    tint = MaterialTheme.colors.topAppBarContentColor
                )
            }
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.primary
    ) {
        TextField(
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search_coins),
                    color = Color.White,
                    modifier = Modifier.alpha(ContentAlpha.medium)
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = { },
                    modifier = Modifier.alpha(ContentAlpha.medium)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "SearchImage",
                        tint = Color.White
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "CloseImage",
                        tint = Color.White
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onCloseClicked()
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
            )
        )
    }
}

@Composable
fun AppBar(
    searchState: SearchAppBarState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    onNavigate: (UIEvent.Navigate) -> Unit
) {
    when (searchState) {
        SearchAppBarState.CLOSED -> {
            DefaultAppBar(
                onSearchClicked = onSearchTriggered,
                onNavigate = onNavigate
            )
        }
        SearchAppBarState.OPENED -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked
                //onSearchClicked = onSearchClicked
            )
        }
    }
}
