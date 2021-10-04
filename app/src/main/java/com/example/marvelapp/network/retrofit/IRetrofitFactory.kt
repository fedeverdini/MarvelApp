package com.example.marvelapp.network.retrofit

import com.example.marvelapp.utils.constants.NetworkUrl.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface IRetrofitFactory {
    fun getOkHttpClient(): OkHttpClient?
    fun makeRetrofit(baseUrl: String = BASE_URL): Retrofit
}