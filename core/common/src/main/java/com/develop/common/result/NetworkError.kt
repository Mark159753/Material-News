package com.develop.common.result

sealed interface NetworkError<out R>: Error {
    data object RequestTimeout:NetworkError<Nothing>
    data class Parsing(val throwable: Throwable):NetworkError<Nothing>
    data class ServerError<out R>(val error:R):NetworkError<R>
    data class UnknownError(val exception:Throwable? = null):NetworkError<Nothing>
    data object EmptyResult:NetworkError<Nothing>
    data object Connectivity:NetworkError<Nothing>
}