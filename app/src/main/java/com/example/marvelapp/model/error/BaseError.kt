package com.example.marvelapp.model.error

import com.example.marvelapp.utils.errors.ErrorCode

data class BaseError(
    val errorCode: ErrorCode?,
    val errorMessageRes: Int? = null,
    val errorMessageString: String? = null
)
