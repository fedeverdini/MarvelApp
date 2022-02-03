package com.example.marvelapp.utils.errors

import com.example.marvelapp.model.error.BaseError
import com.example.marvelapp.model.error.NoInternetException
import com.example.marvelapp.model.error.NoNetworkException
import java.io.IOException
import java.net.SocketTimeoutException

object ErrorUtils {
    private const val ERROR_500 = 500
    private const val ERROR_400 = 400
    private const val ERROR_409 = 409
    private const val ERROR_404 = 404

    fun getErrorFromThrowable(throwable: Throwable): BaseError {
        return when (throwable) {
            is SocketTimeoutException -> BaseError(
                errorCode = ErrorCode.CONNECTION_TIMEOUT,
                errorMessageString = ErrorCode.CONNECTION_TIMEOUT.message
            )
            is NotImplementedError -> BaseError(
                errorCode = ErrorCode.NOT_IMPLEMENTED,
                errorMessageString = throwable.message
            )
            is NoInternetException -> BaseError(
                errorCode = ErrorCode.NO_INTERNET,
                errorMessageString = ErrorCode.NO_INTERNET.message
            )
            is NoNetworkException -> BaseError(
                errorCode = ErrorCode.NO_INTERNET,
                errorMessageString = ErrorCode.NO_INTERNET.message
            )
            is IOException -> BaseError(
                ErrorCode.IO_EXCEPTION,
                errorMessageString = throwable.message
            )
            else -> BaseError(
                errorCode = ErrorCode.UNKNOWN,
                errorMessageString = ErrorCode.UNKNOWN.message
            )
        }
    }

    fun getErrorFromHttpCode(code: Int, message: String): BaseError {
        return when (code) {
            ERROR_500 -> BaseError(
                errorCode = ErrorCode.HTTP500,
                errorMessageString = ErrorCode.HTTP500.message
            )
            ERROR_400 -> BaseError(errorCode = ErrorCode.HTTP400, errorMessageString = message)
            ERROR_409 -> BaseError(errorCode = ErrorCode.HTTP409, errorMessageString = message)
            ERROR_404 -> BaseError(
                errorCode = ErrorCode.HTTP404,
                errorMessageString = ErrorCode.HTTP404.message
            )
            else -> BaseError(
                errorCode = ErrorCode.UNKNOWN,
                errorMessageString = ErrorCode.UNKNOWN.message
            )
        }
    }

    fun createError(error: ErrorCode, message: String? = null): BaseError {
        return BaseError(
            errorCode = error,
            errorMessageString = message ?: error.message
        )
    }
}
