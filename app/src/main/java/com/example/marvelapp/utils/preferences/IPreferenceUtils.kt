package com.example.marvelapp.utils.preferences

interface IPreferenceUtils {
    fun remove(key: String)
    fun putLong(key: String, value: Long)
    fun getLong(key: String): Long
}