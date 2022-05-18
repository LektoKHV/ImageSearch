package ru.vldkrt.imagesearch.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import ru.vldkrt.imagesearch.domain.Resource

suspend fun <T> safeApiCall(
    call: suspend () -> Response<T>
): NetworkResult<T> = try {
    NetworkResult.Success(call())
} catch (e: Exception) {
    NetworkResult.Failure(e)
}

@Suppress("BlockingMethodInNonBlockingContext")
internal inline fun <ResponseType, ResultType> networkBoundResource(
    crossinline remoteCall: suspend () -> Response<ResponseType>,
    crossinline mapper: (ResponseType) -> ResultType,
): Flow<Resource<ResultType>> = flow {
    emit(Resource.Loading(null)) // Show loading at start

    val result: NetworkResult<ResponseType> = safeApiCall { remoteCall.invoke() }

    emit(
        try {
            val response: Response<ResponseType> = result.getOrThrow()

            if (response.isSuccessful) { // Remote call is successful, map it to Resource
                val body: ResponseType? = response.body()

                if (body != null) Resource.Success(mapper(body))
                else Resource.Error("Content is empty")
            } else {
                val httpException = HttpException(response)
                Resource.Error(
                    errorMessage = response.errorBody()?.string().orEmpty(),
                    exception = httpException
                )
            }
        } catch (e: Exception) {
            Resource.Error(errorMessage = e.message, exception = e)
        }
    )
}