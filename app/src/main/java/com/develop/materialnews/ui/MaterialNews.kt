package com.develop.materialnews.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.develop.materialnews.navigation.MaterialNewsNavHost
import kotlinx.coroutines.launch

@Composable
fun MaterialNews(){

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState){
            Snackbar(
                snackbarData = it,
                contentColor = Color.White,
                dismissActionContentColor = Color.White
            )
        } },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(paddingValues)
        ) {
            MaterialNewsNavHost(
                onShowSnackBar = { msg ->
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState
                            .showSnackbar(
                                message = msg,
                                withDismissAction = true
                            )
                    }
                }
            )
        }
    }
}