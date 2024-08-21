package com.develop.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.develop.home.ui.HomeScreen
import com.develop.ui.navigation.FilterNavigationRoute
import com.develop.ui.navigation.HomeNavigationRoute
import com.develop.ui.navigation.SharedAxisDuration
import com.develop.ui.navigation.SharedAxisSlideDiStance
import com.develop.ui.navigation.WebViewNavigationRoute
import soup.compose.material.motion.animation.materialSharedAxisXIn
import soup.compose.material.motion.animation.materialSharedAxisXOut
import soup.compose.material.motion.animation.materialSharedAxisZIn
import soup.compose.material.motion.animation.materialSharedAxisZOut

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HomeNavigationRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onNavBack:()->Unit,
    onShowSnackBar:(String)->Unit,
    onNavToWebView:(link:String)->Unit,
    onNavToFilter:()->Unit = {},
) {
    composable(
        route = HomeNavigationRoute,
        enterTransition = {
            when(initialState.destination.route){
                WebViewNavigationRoute -> materialSharedAxisZIn(forward = false, durationMillis = SharedAxisDuration)
                FilterNavigationRoute -> materialSharedAxisXIn(forward = false, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                else -> null
            }
        },
        exitTransition = {
            when(targetState.destination.route){
                WebViewNavigationRoute -> materialSharedAxisZOut(forward = true, durationMillis = SharedAxisDuration)
                FilterNavigationRoute -> materialSharedAxisXOut(forward = true, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                else -> null
            }
        },
        popEnterTransition = {
            when(initialState.destination.route){
                WebViewNavigationRoute -> materialSharedAxisZIn(forward = false, durationMillis = SharedAxisDuration)
                FilterNavigationRoute -> materialSharedAxisXIn(forward = false, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                else -> null
            }
        },
        popExitTransition = {
            when(targetState.destination.route){
                WebViewNavigationRoute -> materialSharedAxisZOut(forward = true, durationMillis = SharedAxisDuration)
                FilterNavigationRoute -> materialSharedAxisXOut(forward = true, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                else -> null
            }
        }
    ) {
        HomeScreen(
            onShowSnackBar = onShowSnackBar,
            onNavToWebView = onNavToWebView,
            onNavToFilter = onNavToFilter,
            onNavBackStack = onNavBack
        )
    }
}