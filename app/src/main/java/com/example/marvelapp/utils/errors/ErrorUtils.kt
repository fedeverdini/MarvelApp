package com.example.marvelapp.utils.errors

import com.example.marvelapp.model.error.BaseError
import com.example.marvelapp.model.error.NoInternetException
import com.example.marvelapp.model.error.NoNetworkException
import java.io.IOException
import java.net.SocketTimeoutException

abstract class ErrorUtils {
    companion object {
        fun getErrorFromThrowable(throwable: Throwable): BaseError {
            return when (throwable) {
                is SocketTimeoutException -> BaseError(
                    ErrorCode.CONNECTION_TIMEOUT,
                    ErrorCode.CONNECTION_TIMEOUT.message
                )
                is NotImplementedError -> BaseError(ErrorCode.NOT_IMPLEMENTED, throwable.message)
                is NoInternetException -> BaseError(
                    ErrorCode.NO_INTERNET,
                    ErrorCode.NO_INTERNET.message
                )
                is NoNetworkException -> BaseError(
                    ErrorCode.NO_INTERNET,
                    ErrorCode.NO_INTERNET.message
                )
                is IOException -> BaseError(ErrorCode.IO_EXCEPTION, throwable.message)
                else -> BaseError(ErrorCode.UNKNOWN, ErrorCode.UNKNOWN.message)
            }
        }

        fun getErrorFromHttpCode(code: Int, message: String): BaseError {
            return when (code) {
                500 -> BaseError(ErrorCode.HTTP500, ErrorCode.HTTP500.message)
                400 -> BaseError(ErrorCode.HTTP400, message)
                409 -> BaseError(ErrorCode.HTTP409, message)
                404 -> BaseError(ErrorCode.HTTP404, ErrorCode.HTTP404.message)
                else -> BaseError(ErrorCode.UNKNOWN, ErrorCode.UNKNOWN.message)
            }
        }

        fun createError(error: ErrorCode, message: String? = null): BaseError {
            return BaseError(error, message ?: error.message)
        }
    }
}