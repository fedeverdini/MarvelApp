package com.example.marvelapp.utils.date

import com.example.marvelapp.utils.constants.Constants
import java.util.*

class DateUtils : IDateUtils {
    override fun isCacheExpired(lastUpdate: Long?): Boolean {
        return lastUpdate?.let {
            (Date().time - lastUpdate) >= Constants.CACHE_EXPIRATION_TIME
        } ?: true
    }

    override fun Date.extract(millis: Long): Long {
        return this.time - millis
    }
}