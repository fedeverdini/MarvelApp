package com.example.marvelapp.utils.network

interface INetworkUtils {
    fun isNetworkAvailable(): Boolean
    fun isInternetAvailable(): Boolean
}