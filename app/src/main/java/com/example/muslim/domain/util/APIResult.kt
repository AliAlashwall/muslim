package com.example.muslim.domain.util

sealed class APIResult<out T> {
    object Loading : APIResult<Nothing>()
    object Empty : APIResult<Nothing>()

    data class Success<T>(val data: T) : APIResult<T>()
    
    sealed class Error(open val message: String) : APIResult<Nothing>() {
        data class NetworkError(override val message: String = "No internet connection") : Error(message)
        data class TimeoutError(override val message: String = "Request timeout") : Error(message)
        data class ServerError(val code: Int, override val message: String) : Error(message)
        data class ParsingError(override val message: String = "Failed to parse response") : Error(message)
        data class UnknownError(override val message: String) : Error(message)
    }
}

inline fun <T> APIResult<T>.getOrNull(): T? = when (this) {
    is APIResult.Success -> data
    else -> null
}

inline fun <T> APIResult<T>.isSuccess(): Boolean = this is APIResult.Success

inline fun <T> APIResult<T>.isError(): Boolean = this is APIResult.Error

inline fun <T, R> APIResult<T>.map(transform: (T) -> R): APIResult<R> = when (this) {
    is APIResult.Success -> APIResult.Success(transform(data))
    is APIResult.Error -> this
    is APIResult.Loading -> APIResult.Loading
    is APIResult.Empty -> APIResult.Empty
}