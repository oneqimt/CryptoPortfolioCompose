package com.imtmobileapps.cryptocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.imtmobileapps.cryptocompose.ui.theme.CryptoComposeTheme
import com.imtmobileapps.cryptocompose.util.Routes
import com.imtmobileapps.cryptocompose.view.cryptolist.PersonCoinsDetailScreen
import com.imtmobileapps.cryptocompose.view.cryptolist.PersonCoinsListScreen
import com.imtmobileapps.cryptocompose.view.holding.AddHoldingDetailScreen
import com.imtmobileapps.cryptocompose.view.holding.AddHoldingListScreen
import com.imtmobileapps.cryptocompose.view.login.LoginScreen
import com.imtmobileapps.cryptocompose.viewmodel.CryptoListViewModel
import com.imtmobileapps.cryptocompose.viewmodel.LoginViewModel
import com.imtmobileapps.cryptocompose.viewmodel.ManageHoldingsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@ExperimentalAnimationApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    private val viewModel: CryptoListViewModel by viewModels()
    private val holdingsViewModel: ManageHoldingsViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CryptoComposeTheme {

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.LOGIN_SCREEN
                ) {

                    composable(
                        Routes.LOGIN_SCREEN
                    ) {
                        LoginScreen(viewModel = loginViewModel, onNavigate = {
                            navController.navigate(Routes.PERSON_COINS_LIST)
                        })
                    }

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

                        }, viewModel = viewModel)
                    }

                    composable(
                        Routes.ADD_HOLDING_LIST
                    ) {
                        AddHoldingListScreen(onPopBackStack = {
                            navController.popBackStack()
                        }, viewModel = holdingsViewModel, onNavigate = {
                            navController.navigate(Routes.ADD_HOLDING_DETAIL)
                        })
                    }

                    composable(
                        Routes.ADD_HOLDING_DETAIL
                    ) {
                        AddHoldingDetailScreen(viewModel = holdingsViewModel, onPopBackStack = {
                            navController.popBackStack()

                        })
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