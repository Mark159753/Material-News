package com.develop.home.ui

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.develop.common.constants.AppLanguage

@Composable
internal fun LanguagePopupOptions(
    modifier: Modifier = Modifier,
    currentLocale:AppLanguage = AppLanguage.EN,
    expanded:Boolean = false,
    onDismissRequest:()->Unit = {},
    onClick:(AppLanguage)->Unit = {}
){
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer),
    ) {
        AppLanguage.entries.forEach { lng ->
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = lng.titleRes))
                },
                trailingIcon = if (currentLocale == lng) {
                    { Icon(imageVector = Icons.Default.Check, contentDescription = "check") }
                } else null,
                onClick = {
                    onClick(lng)
                },
                modifier = Modifier
                    .semantics {
                        contentDescription = "lngItem ${lng.name}"
                    }
            )
        }
    }
}