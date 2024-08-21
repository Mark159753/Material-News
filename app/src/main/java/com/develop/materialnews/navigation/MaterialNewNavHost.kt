package com.develop.materialnews.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.develop.filters.navigation.filterScreen
import com.develop.filters.navigation.navigateToFilter
import com.develop.home.navigation.homeScreen
import com.develop.ui.navigation.HomeNavigationRoute
import com.develop.webview.navigation.navigateToWebView
import com.develop.webview.navigation.webViewScreen

@Composable
fun MaterialNewsNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination:String = HomeNavigationRoute,
    onShowSnackBar:(String)->Unit,
){
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination,
    ){
        homeScreen(
            onShowSnackBar = onShowSnackBar,
            onNavToWebView = { link ->
                if (navController.currentBackStackEntry?.lifecycleIsResumed() == true) {
                    navController.navigateToWebView(
                        link = link
                    )
                }
            },
            onNavToFilter = {
                if (navController.currentBackStackEntry?.lifecycleIsResumed() == true) {
                    navController.navigateToFilter()
                }
            },
            onNavBack = {
                if (navController.currentBackStackEntry?.lifecycleIsResumed() == true) {
                    navController.navigateUp()
                }
            }
        )

        filterScreen(
            onShowSnackBar = onShowSnackBar,
            onNavBack = {
                if (navController.currentBackStackEntry?.lifecycleIsResumed() == true) {
                    navController.navigateUp()
                }
            }
        )

        webViewScreen(
            onNavBack = {
                if (navController.currentBackStackEntry?.lifecycleIsResumed() == true) {
                    navController.navigateUp()
                }
            }
        )
    }
}


private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED