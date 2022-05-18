package ru.vldkrt.imagesearch.data

import retrofit2.Response

sealed class NetworkResult<T> {

    /** Returns response, if result of remote call is successful, or throws the exception */
    fun getOrThrow(): Response<T> {
        when (this) {
            is Success<T> -> return response
            is Failure<T> -> throw exception
        }
    }

    data class Success<T>(val response: Response<T>): NetworkResult<T>()
    data class Failure<T>(val exception: Throwable): NetworkResult<T>()
}