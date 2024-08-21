package com.develop.network.until

import com.develop.common.result.NetworkError
import com.google.gson.Gson
import retrofit2.Response
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException
import com.develop.common.result.Result
import com.develop.network.models.error.ErrorResponse
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken

typealias NetworkResponse<T, E> = Result<T, NetworkError<E>>

typealias GenericNetworkResponse<T> = NetworkResponse<T, ErrorResponse>

suspend fun <T : Any, E> safeApiCall(api:suspend () -> Response<T>):NetworkResponse<T, E>{
    return try {
        val response = api.invoke()
        if (response.isSuccessful){
            val body = response.body()
            when{
                body == null || response.code() == 204 -> Result.Error(NetworkError.EmptyResult)
                response.code() == 408 -> Result.Error(NetworkError.RequestTimeout)
                else -> Result.Success(body)
            }
        }else{
            parseErrorBody(response)
        }
    }catch (e:CancellationException){
        throw e
    }catch (e: UnknownHostException){
        Result.Error(NetworkError.Connectivity)
    }catch (e: JsonParseException){
        Result.Error(NetworkError.Parsing(e))
    }
    catch (e:Exception){
        Result.Error(NetworkError.UnknownError(e))
    }
}

private fun <T : Any, E : Any> parseErrorBody(response: Response<T>?): NetworkResponse<T, E> {
    return try {
        val body = response?.errorBody()?.string() ?: return Result.Error(NetworkError.UnknownError())
        val gson = Gson()
        val type = object : TypeToken<E>() {}.type
        val error: E = gson.fromJson(body, type)
        Result.Error(NetworkError.ServerError(error))
    } catch (e: Exception) {
        Result.Error(NetworkError.UnknownError(e))
    }
}