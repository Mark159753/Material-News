package com.develop.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.develop.ui.R
import com.develop.ui.theme.Gray18
import com.develop.ui.theme.MaterialNewsTheme
import com.develop.ui.theme.White

@Composable
fun SettingsButton(
    modifier: Modifier = Modifier,
    title:String,
    @DrawableRes
    iconRes:Int,
    onClick:()->Unit = {},
    colors:SettingsButtonColors = settingsButtonColors(),
    isActive:Boolean = false,
    haveOptions:Boolean = false,
    menu:(@Composable ()->Unit)? = null
){

    val containerColor:Color by animateColorAsState(
        targetValue = if (isActive) colors.selectedContainer else colors.container,
        label = "containerColor"
    )

    val contentColor:Color by animateColorAsState(
        targetValue = if (isActive) colors.onSelectedContainer else colors.onContainer
    )

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(25.dp))
            .clickable(onClick = onClick)
            .background(containerColor)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            tint = contentColor,
            modifier = Modifier
                .size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = contentColor
        )

        if (haveOptions) {
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = "arrow",
                tint = contentColor
            )

            menu?.invoke()
        }
    }
}

@Immutable
data class SettingsButtonColors(
    val container:Color,
    val onContainer:Color,
    val selectedContainer:Color,
    val onSelectedContainer:Color,
)

@Composable
fun settingsButtonColors(
    container:Color = Gray18,
    onContainer:Color = White,
    selectedContainer:Color = MaterialTheme.colorScheme.primaryContainer,
    onSelectedContainer:Color = Color.Black,
):SettingsButtonColors = SettingsButtonColors(
    container = container,
    onContainer = onContainer,
    selectedContainer = selectedContainer,
    onSelectedContainer = onSelectedContainer
)

@Preview
@Composable
private fun LngSettingsButton(){
    MaterialNewsTheme {
        SettingsButton(
            title = stringResource(id = R.string.home_screen_language_bts_title),
            iconRes = R.drawable.language_ic,
            haveOptions = true
        )
    }
}

@Preview
@Composable
private fun ThemeSettingsButton(){
    MaterialNewsTheme {
        SettingsButton(
            title = stringResource(id = R.string.home_screen_dark_theme_bts_title),
            iconRes = R.drawable.dark_theme_ic,
        )
    }
}