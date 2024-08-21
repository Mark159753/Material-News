package com.develop.webview.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.develop.ui.navigation.HomeNavigationRoute
import com.develop.ui.navigation.SharedAxisDuration
import com.develop.ui.navigation.UrlArg
import com.develop.ui.navigation.WebViewNavigationRoute
import com.develop.webview.ui.WebViewRoute
import soup.compose.material.motion.animation.materialSharedAxisZIn
import soup.compose.material.motion.animation.materialSharedAxisZOut

fun NavController.navigateToWebView(link:String, navOptions: NavOptions? = null) {
    val encoded =  Uri.encode(link)
    val route = "web_view_navigation_route/$encoded"
    this.navigate(route, navOptions)
}

fun NavGraphBuilder.webViewScreen(
    onNavBack:()->Unit
) {
    composable(
        route = WebViewNavigationRoute,
        arguments = listOf(
            navArgument(UrlArg) { type = NavType.StringType },
        ),
        enterTransition = {
            when(initialState.destination.route){
                HomeNavigationRoute -> materialSharedAxisZIn(forward = true, durationMillis = SharedAxisDuration)
                else -> null
            }
        },
        exitTransition = {
            when(targetState.destination.route){
                HomeNavigationRoute -> materialSharedAxisZOut(forward = false, durationMillis = SharedAxisDuration)
                else -> null
            }
        },
        popEnterTransition = {
            when(initialState.destination.route){
                HomeNavigationRoute -> materialSharedAxisZIn(forward = false, durationMillis = SharedAxisDuration)
                else -> null
            }
        },
        popExitTransition = {
            when(targetState.destination.route){
                HomeNavigationRoute -> materialSharedAxisZOut(forward = false, durationMillis = SharedAxisDuration)
                else -> null
            }
        }
    ) { backStackEntry ->
        WebViewRoute(
            link = Uri.decode(backStackEntry.arguments?.getString(UrlArg)!!),
            onNavBack = onNavBack
        )
    }
}