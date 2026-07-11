package com.example.muslim.data.util

import com.example.muslim.domain.util.APIResult
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ResponseException
import kotlinx.serialization.SerializationException
import java.net.SocketException
import java.net.UnknownHostException

object HttpErrorHandler {
    fun handleException(exception: Exception, defaultMessage: String): APIResult.Error {
        return when (exception) {
            is ConnectTimeoutException, is SocketTimeoutException -> {
                APIResult.Error.TimeoutError("Request timed out. Please try again.")
            }
            is ResponseException -> {
                val statusCode = exception.response.status.value
                val statusMessage = exception.response.status.description
                when (statusCode) {
                    in 400..499 -> APIResult.Error.ServerError(
                        code = statusCode,
                        message = "Client error ($statusCode): $statusMessage"
                    )
                    in 500..599 -> APIResult.Error.ServerError(
                        code = statusCode,
                        message = "Server error ($statusCode): $statusMessage"
                    )
                    else -> APIResult.Error.ServerError(
                        code = statusCode,
                        message = statusMessage
                    )
                }
            }
            is SerializationException -> {
                APIResult.Error.ParsingError("Failed to parse server response. Please try again.")
            }
            is UnknownHostException, is SocketException -> {
                APIResult.Error.NetworkError("No internet connection. Please check your network.")
            }
            else -> {
                APIResult.Error.UnknownError(exception.localizedMessage ?: defaultMessage)
            }
        }
    }
}
