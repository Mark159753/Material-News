package com.develop.materialnews.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MaterialNews(){

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState){
            Snackbar(
                snackbarData = it,
                contentColor = Color.White,
                dismissActionContentColor = Color.White
            )
        } },
    ) { _ ->
        Column(
            modifier = Modifier
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