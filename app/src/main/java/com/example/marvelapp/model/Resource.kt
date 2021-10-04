package com.example.marvelapp.model

import com.example.marvelapp.BuildConfig
import com.example.marvelapp.model.error.BaseError
import com.example.marvelapp.utils.errors.ErrorCode
import com.example.marvelapp.utils.errors.ErrorUtils
import com.google.gson.Gson
import retrofit2.Response
import timber.log.Timber

data class Resource<out T>(val status: Status, val data: T? = null, val error: BaseError? = null) {
    enum class Status {
        //LOADING,
        SUCCESS,
        ERROR
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data = data)
        }

        fun <T> error(error: BaseError): Resource<T> {
            return Resource(Status.ERROR, error = error)
        }

        /*fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING)
        }*/

        fun <T> create(response: Response<T>): Resource<T> {
            if (response.isSuccessful) {
                val obj: T? = response.body()
                if (obj == null) {
                    return success(obj)
                }

                val gson = Gson()
                val basic = gson.fromJson(gson.toJson(obj), BasicResponse::class.java)
                return if (basic.status == "Ok") {
                    success(obj)
                } else {
                    val customError = BaseError(ErrorCode.UNKNOWN, "Unexpected error")
                    error(customError)
                }
            } else {
                val error = ErrorUtils.getErrorFromHttpCode(response.code(), response.message())
                return error(error)
            }
        }

        fun <T> create(throwable: Throwable): Resource<T> {
            if (BuildConfig.DEBUG) Timber.d(throwable)
            return error(ErrorUtils.getErrorFromThrowable(throwable))
        }
    }
}