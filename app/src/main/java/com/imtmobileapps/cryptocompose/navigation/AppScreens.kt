package com.imtmobileapps.cryptocompose.navigation

import androidx.navigation.NavHostController
import com.imtmobileapps.cryptocompose.util.Routes
import com.imtmobileapps.cryptocompose.util.Routes.SPLASH

class AppScreens(navController: NavHostController) {

    val splash: () -> Unit = {
        navController.navigate(route = Routes.PERSON_COINS_LIST) {
            popUpTo(SPLASH) { inclusive = true }
        }
    }

    val personCoinsList: () -> Unit = {
        navController.navigate(route = Routes.PERSON_COINS_DETAIL)
    }

    val personCoinsDetail: () -> Unit = {

    }

}