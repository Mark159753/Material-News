package com.develop.materialnews.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.develop.ui.theme.MaterialNewsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel:MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        initCurrentLocale()
        setContent {

            val isDarkTheme by viewModel.isSystemInDark.collectAsStateWithLifecycle()

            MaterialNewsTheme(
                darkTheme = isDarkTheme ?: isSystemInDarkTheme()
            ) {
                MaterialNews()
            }
        }
    }

    private fun initCurrentLocale(){
        lifecycleScope.launch {
            viewModel.initCurrentLocale(this@MainActivity)
        }
    }
}