package com.develop.ui.util.actions

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.develop.common.result.NetworkError
import com.develop.network.models.error.ErrorResponse
import com.develop.ui.R
import com.develop.ui.util.strings.UIText
import com.develop.ui.util.strings.toUIText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

interface UIActions {

    val actions: Flow<Action>

    val errorDialog:MutableState<DisplayErrorDialog?>

    fun hideErrorDialog()

    suspend fun sendAction(action: Action)

    suspend fun handleApiError(error: NetworkError<ErrorResponse>)
}

class UIActionsImpl:UIActions{

    private val _actions = Channel<Action>()
    override val actions: Flow<Action>
        get() = _actions.receiveAsFlow()

    override val errorDialog: MutableState<DisplayErrorDialog?> = mutableStateOf(null)

    override suspend fun sendAction(action: Action) {
        _actions.send(action)
    }

    override fun hideErrorDialog() {
        errorDialog.value = null
    }

    override suspend fun handleApiError(error: NetworkError<ErrorResponse>) {
        val msg = error.toUIText()
        when(error){
            NetworkError.Connectivity,
            NetworkError.RequestTimeout -> sendAction(CommonAction.ShowSnackBar(msg = msg))
            is NetworkError.Parsing,
            is NetworkError.ServerError,
            is NetworkError.UnknownError -> {
                errorDialog.value = DisplayErrorDialog(
                    msg = msg,
                    title = UIText.ResString(R.string.error_dialog_title)
                )
            }
            else -> {}
        }
    }
}