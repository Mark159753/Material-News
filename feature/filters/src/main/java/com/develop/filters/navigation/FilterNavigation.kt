package com.develop.filters.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.develop.filters.ui.FilterRoute
import com.develop.filters.ui.FilterViewModel
import com.develop.ui.navigation.FilterNavigationRoute
import com.develop.ui.navigation.HomeNavigationRoute
import com.develop.ui.navigation.SharedAxisDuration
import com.develop.ui.navigation.SharedAxisSlideDiStance
import com.develop.ui.navigation.WebViewNavigationRoute
import soup.compose.material.motion.animation.materialSharedAxisXIn
import soup.compose.material.motion.animation.materialSharedAxisXOut
import soup.compose.material.motion.animation.materialSharedAxisZIn
import soup.compose.material.motion.animation.materialSharedAxisZOut

fun NavController.navigateToFilter(navOptions: NavOptions? = null) {
    this.navigate(FilterNavigationRoute, navOptions)
}

fun NavGraphBuilder.filterScreen(
    onShowSnackBar:(String)->Unit,
    onNavBack:()->Unit = {},
) {
    composable(
        route = FilterNavigationRoute,
        enterTransition = {
            when(initialState.destination.route){
                WebViewNavigationRoute -> materialSharedAxisZIn(forward = false, durationMillis = SharedAxisDuration)
                HomeNavigationRoute -> materialSharedAxisXIn(forward = true, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                else -> null
            }
        },
        exitTransition = {
            when(targetState.destination.route){
                WebViewNavigationRoute -> materialSharedAxisZOut(forward = true, durationMillis = SharedAxisDuration)
                HomeNavigationRoute -> materialSharedAxisXOut(forward = false, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                else -> null
            }
        },
        popEnterTransition = {
            when(initialState.destination.route){
                WebViewNavigationRoute -> materialSharedAxisZIn(forward = false, durationMillis = SharedAxisDuration)
                HomeNavigationRoute -> materialSharedAxisXIn(forward = false, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                else -> null
            }
        },
        popExitTransition = {
            when(targetState.destination.route){
                WebViewNavigationRoute -> materialSharedAxisZOut(forward = true, durationMillis = SharedAxisDuration)
                HomeNavigationRoute -> materialSharedAxisXOut(forward = false, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                else -> null
            }
        }
    ) {

        val viewModel:FilterViewModel = hiltViewModel()

        FilterRoute(
            onNavBack = onNavBack,
            onShowSnackBar = onShowSnackBar,
            state = viewModel.state
        )
    }
}