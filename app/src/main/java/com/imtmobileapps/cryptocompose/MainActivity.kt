package com.imtmobileapps.cryptocompose

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.imtmobileapps.cryptocompose.components.CircularProgressBar
import com.imtmobileapps.cryptocompose.data.CryptoApiState
import com.imtmobileapps.cryptocompose.ui.theme.CryptoComposeTheme
import com.imtmobileapps.cryptocompose.viewmodel.CryptoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
@ExperimentalAnimationApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    private val viewModel: CryptoListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       /* lifecycleScope.launch {
            viewModel.getPersonCoins(3)
        }*/

        setContent {
            CryptoComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting(
                        this@MainActivity,
                        viewModel,
                        name = "Dennis",
                        onButtonClicked = {
                              viewModel.personCoins.value.data.let {
                                  println("BUTTON clicked $it")
                              }
                        }

                    )
                }
            }
        }

    }

}


@Composable
fun Greeting(
    context: Context,
    viewModel: CryptoListViewModel,
    name: String,
    onButtonClicked: (String) -> Unit
) {

    val personCoins = viewModel.personCoins.collectAsState()

    println("in GREETING and personCoins is : ${personCoins.value}")

    Scaffold(

        topBar = {
            TopAppBar { /* Top app bar content */ }
        },
        content = {
            when (personCoins.value.status) {
                CryptoApiState.Status.LOADING -> {

                    println("GREETING and LOADING show progress bar")
                    Column(
                        modifier = Modifier.padding(30.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        CircularProgressBar()
                    }
                }

                CryptoApiState.Status.SUCCESS ->{
                    println("SUCCESS show list")
                    LaunchedEffect(key1 = true ){
                        viewModel.personCoins.value.data.let {
                            println("GREETING and show the list $it")
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(30.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally


                    ){
                        Text(text = "Hello $name!")

                        Button(onClick = { onButtonClicked("") }) {

                        }
                    }
                }

                // show error
                else ->
                    Toast.makeText(context, "${personCoins.value.message}", Toast.LENGTH_SHORT).show()

            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CryptoComposeTheme {
        //Greeting("Compose")
    }
}