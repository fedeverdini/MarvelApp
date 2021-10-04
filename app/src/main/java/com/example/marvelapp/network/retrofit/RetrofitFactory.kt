package com.example.marvelapp.network.retrofit

import com.example.marvelapp.BuildConfig
import com.example.marvelapp.network.RequestInterceptor
import com.example.marvelapp.network.retrofit.adapter.CustomCallAdapterFactory
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory : IRetrofitFactory {
    private var okHttpClient: OkHttpClient? = null

    override fun makeRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient()!!)
            .addCallAdapterFactory(CustomCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    override fun getOkHttpClient(): OkHttpClient? {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(RequestInterceptor())
                .addInterceptor(getHttpLoggingInterceptor())
                .build()
        }

        return okHttpClient
    }

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
}