package com.imtmobileapps.cryptocompose.view.login

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.imtmobileapps.cryptocompose.R
import com.imtmobileapps.cryptocompose.components.LoginCard
import com.imtmobileapps.cryptocompose.event.UIEvent
import com.imtmobileapps.cryptocompose.model.Credentials
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarBackgroundColor
import com.imtmobileapps.cryptocompose.ui.theme.topAppBarContentColor
import com.imtmobileapps.cryptocompose.util.deleteSensitiveFile
import com.imtmobileapps.cryptocompose.util.readUsernameAndPassword
import com.imtmobileapps.cryptocompose.util.writeUsernameAndPassword
import com.imtmobileapps.cryptocompose.viewmodel.CryptoListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import logcat.logcat
import java.io.FileNotFoundException

@Composable
fun LoginScreen(
    viewModel: CryptoListViewModel,
    onNavigate: () -> Unit,
) {

    val TAG = "LoginScreen"

    val usernameText = rememberSaveable {
        mutableStateOf("")
    }

    val passwordText = rememberSaveable {
        mutableStateOf("")
    }

    val checked = rememberSaveable {
        mutableStateOf(false)
    }

    val scaffoldState =
        rememberScaffoldState() // This is here in case we want to display a snackbar

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val credentials = remember {
        mutableStateOf(Credentials(username = "", password = ""))
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.Navigate -> {
                    logcat(TAG) { "UIEvent.Navigate called in LoginScreen and route is ${event.route}" }
                    onNavigate()
                }

                is UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                }

                else -> Unit
            }
        }
    }
    // Check if the user has the credentials cached, if so, log them in
    LaunchedEffect(key1 = credentials.value) {
        try {
            // READ
            val auth = readUsernameAndPassword(context = context)
            logcat(TAG) { "LaunchedEffect AUTH from SANDBOX is : ${auth}" }
            val test = auth.split(":")
            credentials.value.username = test[0]
            credentials.value.password = test[1]
            logcat(TAG) { "LaunchedEffect SPLIT is  : ${test[0]} ${test[1]}" }

            logcat(TAG) { "LaunchedEffect Credentials object is  : ${credentials.value}" }

            // go ahead and log them in
            logcat(TAG) {
                "calling viewModel.LOGIN from  LaunchedEffect : ${credentials.value.username} ${credentials.value.password}"
            }
            viewModel.login(credentials.value.username, credentials.value.password)

        } catch (e: FileNotFoundException) {
            logcat(TAG) { "FileNotFoundException ${e.localizedMessage as String}" }
        } catch (e: Exception) {
            logcat(TAG) { "READ PROBLEM ${e.localizedMessage as String}" }
        }
    }

    Scaffold(scaffoldState = scaffoldState,
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
                    // TODO call login here as well
                    // viewModel.login(usernameText.value, passwordText.value)
                    logcat(TAG) { "onDoneClicked! and username is : ${usernameText.value}" }
                    logcat(TAG) { "onDoneClicked! and password is : ${passwordText.value}" }
                },
                onSignInClicked = {
                    // if file exists, get the uname and password from it, then login
                    logcat(TAG) { "OnSignInClicked and CREDENTIALS are : ${credentials.value}" }
                    if (credentials.value.username.isNotEmpty() && credentials.value.password.isNotEmpty()) {
                        logcat(TAG) {
                            "calling viewModel.LOGIN with FILE values : ${credentials.value.username} ${credentials.value.password}"
                        }
                        viewModel.login(credentials.value.username, credentials.value.password)
                    } else {
                        // else get the login values from the text fields
                        logcat(TAG) { "calling viewModel.LOGIN with INPUT TEXT FIELD values ${usernameText.value} ${passwordText.value}" }
                        viewModel.login(usernameText.value, passwordText.value)
                    }
                },
                onForgotPasswordClicked = {
                    logcat(TAG) { "onForgotPasswordClicked!" }

                },
                onCreateAccountClicked = {
                    logcat(TAG) { "onCreateAccountClicked!" }

                },

                onRememberMeChecked = {
                    checked.value = it
                    logcat(TAG) { "CHECKED VALUE IS:  ${checked.value}" }
                    scope.launch {
                        if (!checked.value) {
                            // delete the file
                            try {
                                deleteSensitiveFile(context = context)
                            } catch (e: Exception) {
                                logcat(TAG) { "Problem resetting ${e.localizedMessage as String}" }
                            }

                        } else {
                            // write it
                            try {
                                writeUsernameAndPassword(context = context,
                                    usernameText.value,
                                    passwordText.value)
                                logcat(TAG) { "Write File Success" }
                            } catch (e: Exception) {
                                // notify user that the file already exists
                                logcat(TAG) { "Problem writing file ${e.localizedMessage as String}" }
                            }
                        }
                    }
                },
                checked = checked.value
            )// end card

        }
    )// end Scaffold
}