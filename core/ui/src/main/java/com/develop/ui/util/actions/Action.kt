package com.develop.ui.util.actions

import androidx.compose.runtime.Immutable
import com.develop.ui.util.strings.UIText

sealed interface Action

sealed interface CommonAction:Action{
    data class ShowSnackBar(val msg:UIText):CommonAction

    data object NavBack:CommonAction
}

@Immutable
data class DisplayErrorDialog(
    val msg:UIText,
    val title:UIText? = null
)