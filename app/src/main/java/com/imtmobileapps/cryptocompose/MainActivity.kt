package com.imtmobileapps.cryptocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.imtmobileapps.cryptocompose.ui.theme.CryptoComposeTheme
import com.imtmobileapps.cryptocompose.util.Routes
import com.imtmobileapps.cryptocompose.view.cryptolist.PersonCoinsDetailScreen
import com.imtmobileapps.cryptocompose.view.cryptolist.PersonCoinsListScreen
import com.imtmobileapps.cryptocompose.viewmodel.CryptoListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@ExperimentalAnimationApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    private val viewModel: CryptoListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CryptoComposeTheme {

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.PERSON_COINS_LIST
                ) {
                    composable(Routes.PERSON_COINS_LIST) {
                        PersonCoinsListScreen(
                            onNavigate = {
                                navController.navigate(it.route)
                                println("MAIN Activity and onNavigate called and route is : $it.route")
                            }, viewModel
                        )
                    }
                    composable(
                        // Not used now, but pass the CryptoValue id argument in case we want to retrieve from DB later
                        route = Routes.PERSON_COINS_DETAIL + "?id={id}",
                        arguments = listOf(
                            navArgument(name = "id") {
                                type = NavType.StringType
                                defaultValue = ""
                            }
                        )
                    ) {
                        PersonCoinsDetailScreen(onPopBackStack = {
                            navController.popBackStack()

                        }, navigateToListScreen = {navController.popBackStack()}, viewModel = viewModel)
                    }
                }//end nav host
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CryptoComposeTheme {

    }
}