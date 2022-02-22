package com.imtmobileapps.cryptocompose.components

import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imtmobileapps.cryptocompose.R
import com.imtmobileapps.cryptocompose.ui.theme.cardBackgroundColor
import com.imtmobileapps.cryptocompose.ui.theme.cardBorderColor
import logcat.logcat

@Composable
fun LoginCard(
    usernameText: String,
    passwordText: String,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onDone: () -> Unit,
    onSignInClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onCreateAccountClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp),
        elevation = 4.dp,
        border = BorderStroke(0.3.dp, MaterialTheme.colors.cardBorderColor),
        backgroundColor = MaterialTheme.colors.cardBackgroundColor,
        shape = RoundedCornerShape(corner = CornerSize(6.dp))
    ) {

        val focusManager = LocalFocusManager.current
        val TAG = "LoginScreen"

        val passwordVisibility = remember { mutableStateOf(false) }

        Column(
            modifier =
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(10.dp, 20.dp))
        {

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = usernameText,
                    label = { Text(text = stringResource(id = R.string.user_name)) },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),

                    onValueChange = {
                        onUsernameChanged(it)
                    },
                )
            }// end 1st row
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center)
            {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = passwordText,
                    onValueChange = {
                        onPasswordChanged(it)
                    },
                    label = { (Text(text = stringResource(id = R.string.password))) },
                    placeholder = { Text(text = stringResource(id = R.string.password)) },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        // TODO add validation
                        logcat(TAG) { " username is $usernameText password is : $passwordText" }
                        onDone()
                    }),
                    visualTransformation =
                    if (passwordVisibility.value) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisibility.value) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        }
                        IconButton(onClick = {
                            passwordVisibility.value = !passwordVisibility.value
                        }) {
                            Icon(imageVector = image, null)
                        }
                    }

                )
            }// end 2nd row

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center)
            {
                TextButton(onClick = { 
                    onForgotPasswordClicked()
                }, enabled = true) {
                    Text(text = stringResource(id = R.string.forgot_password))
                }
            }// end 3rd row

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center)
            {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                       onSignInClicked()
                    })
                {
                    Text(text = stringResource(id = R.string.sign_in))
                }

            }// end 4th row

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center)
            {
                Text(text = stringResource(id = R.string.do_not_have_account))

            }// end 5th row

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center)
            {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                       onCreateAccountClicked()
                    })
                {
                    Text(text = stringResource(id = R.string.create_account))
                }

            }// end 6th row



        }

    }
}

@Preview
@Composable
fun LoginCardPreview() {
    val username = "test1"
    val password = "pass"
    LoginCard(usernameText = username,
        passwordText = password,
        onUsernameChanged = {},
        onPasswordChanged = {},
        onDone = {},
        onSignInClicked = {},
        onForgotPasswordClicked = {},
        onCreateAccountClicked = {}
    )
}

