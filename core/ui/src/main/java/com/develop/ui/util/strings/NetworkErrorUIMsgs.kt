package com.develop.ui.util.strings

import com.develop.common.result.NetworkError
import com.develop.network.models.error.ErrorResponse
import com.develop.ui.R

fun NetworkError<ErrorResponse>.toUIText():UIText = when(this){
    NetworkError.Connectivity -> UIText.ResString(R.string.network_error_connectivity)
    NetworkError.EmptyResult -> UIText.ResString(R.string.network_error_empty_result)
    is NetworkError.Parsing -> UIText.ResString(R.string.network_error_connectivity, arrayOf(throwable.message ?: ""))
    NetworkError.RequestTimeout -> UIText.ResString(R.string.network_error_timeout)
    is NetworkError.ServerError -> UIText.ResString(R.string.network_error_server, arrayOf(error.message))
    is NetworkError.UnknownError -> UIText.ResString(R.string.network_error_unknown, arrayOf(exception?.message ?: ""))
}