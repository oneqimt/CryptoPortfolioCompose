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
import com.imtmobileapps.cryptocompose.viewmodel.ManageHoldingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import logcat.logcat


@AndroidEntryPoint
@ExperimentalAnimationApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    private val viewModel: CryptoListViewModel by viewModels()
    private val holdingsViewModel: ManageHoldingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CryptoComposeTheme {

                val navController = rememberNavController()
                // popUpTo: https://developer.android.com/jetpack/compose/navigation
                NavHost(
                    navController = navController,
                    startDestination = Routes.LOGIN_SCREEN,

                    ) {

                    composable(
                        Routes.LOGIN_SCREEN
                    ) {
                        LoginScreen(viewModel = viewModel, onNavigate = {
                            navController.navigate(Routes.PERSON_COINS_LIST) {
                                popUpTo(Routes.LOGIN_SCREEN) { inclusive = true }
                            }
                        })
                    }

                    composable(Routes.PERSON_COINS_LIST) {
                        PersonCoinsListScreen(
                            onNavigate = {
                                navController.navigate(it.route)
                            }, viewModel
                        )
                    }
                    composable(
                        // Not used now, but we may later
                        route = Routes.PERSON_COINS_DETAIL + "?cmcId={cmcId}",
                        arguments = listOf(
                            navArgument(name = "cmcId") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        // test, can be passed to Composable if necessary
                        val cmcId = it.arguments!!.getInt("cmcId")
                        logcat(TAG) { "ARGUMENT passed is : $cmcId" }
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

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CryptoComposeTheme {

    }
}