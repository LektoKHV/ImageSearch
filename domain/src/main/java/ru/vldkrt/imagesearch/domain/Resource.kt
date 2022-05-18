package ru.vldkrt.imagesearch.domain

/** Representation of any external data, which can be delivered (or not) in continuous time */
sealed class Resource<T>(open val data: T? = null) {

    // It has to contain non-empty data
    class Success<T>(override val data: T) : Resource<T>(data)

    class Loading<T>(data: T? = null) : Resource<T>(data)

    class Error<T>(
        val errorMessage: String? = null,
        data: T? = null,
        val exception: Throwable? = null
    ) : Resource<T>(data)

    // Map function for resource if type change is required
    fun <R> map(transform: (data: T) -> R): Resource<R> {
        val nullableValue: R? = data?.let { transform(it) }

        return when (this) {
            is Success -> Success(nullableValue!!)
            is Loading -> Loading(nullableValue)
            is Error -> Error(errorMessage, nullableValue, exception)
        }
    }
}
