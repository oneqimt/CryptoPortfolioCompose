package com.imtmobileapps.cryptocompose.view.login

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import com.imtmobileapps.cryptocompose.R
import com.imtmobileapps.cryptocompose.components.LoginCard
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarContentColor
import com.imtmobileapps.cryptocompose.viewmodel.CryptoListViewModel
import kotlinx.coroutines.flow.collect
import logcat.logcat

@Composable
fun LoginScreen(
    viewModel: CryptoListViewModel,
    onNavigate: () -> Unit
) {

    val TAG = "LoginScreen"

    val usernameText = rememberSaveable {
        mutableStateOf("")
    }

    val passwordText = rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.Navigate -> {
                    logcat(TAG){"UIEvent.Navigate called in LoginScreen and route is ${event.route}"}
                    onNavigate()
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(

                title = {
                    Text(
                        text = stringResource(id = R.string.login),
                        color = MaterialTheme.colors.topAppBarContentColor
                    )
                },
                backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor

            )
        },

        content = {
            LoginCard(usernameText = usernameText.value,
                passwordText = passwordText.value,
                onUsernameChanged = {
                    usernameText.value = it
                },
                onPasswordChanged = {
                    passwordText.value = it
                },
                onDone = {
                    logcat(TAG) { "onDoneClicked! and username is : ${usernameText.value}" }
                    logcat(TAG) { "onDoneClicked! and password is : ${passwordText.value}" }
                },
                onSignInClicked = {
                    logcat(TAG) { "onSignInClicked! and username is : ${usernameText.value}" }
                    logcat(TAG) { "onSignInClicked! and password is : ${passwordText.value}" }
                    viewModel.login(usernameText.value, passwordText.value)

                },
                onForgotPasswordClicked = {
                    logcat(TAG) { "onForgotPasswordClicked!" }
                },
                onCreateAccountClicked = {
                    logcat(TAG) { "onCreateAccountClicked!" }
                }
            )
        })


}