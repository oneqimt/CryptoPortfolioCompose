package com.imtmobileapps.cryptocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.imtmobileapps.cryptocompose.ui.theme.CryptoComposeTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@ExperimentalAnimationApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CryptoComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting(


                    )
                }
            }
        }

    }

}


@Composable
fun Greeting(

) {

    Scaffold(

        topBar = {
            TopAppBar { /* Top app bar content */ }
        },
        content = {

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