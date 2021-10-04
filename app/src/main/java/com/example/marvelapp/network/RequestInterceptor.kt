package com.example.marvelapp.network

import com.example.marvelapp.model.error.NoInternetException
import com.example.marvelapp.model.error.NoNetworkException
import com.example.marvelapp.utils.encrypt.IEncryptUtils
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
import com.example.marvelapp.utils.constants.Keys.PUBLIC_KEY
import com.example.marvelapp.utils.constants.HttpHeaders.ACCEPT_HEADER
import com.example.marvelapp.utils.constants.HttpHeaders.ACCEPT_HEADER_VALUE
import com.example.marvelapp.utils.network.INetworkUtils
import timber.log.Timber

class RequestInterceptor : Interceptor, KoinComponent {

    private val encryptUtils: IEncryptUtils by inject()
    private val networkUtils: INetworkUtils by inject()

    companion object {
        private const val TS_QUERY_PARAM = "ts"
        private const val KEY_QUERY_PARAM = "apikey"
        private const val HASH_QUERY_PARAM = "hash"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkUtils.isNetworkAvailable()) throw NoNetworkException()
        if (!networkUtils.isInternetAvailable()) throw NoInternetException()

        val original = chain.request()

        val ts = Date().time
        val hash = encryptUtils.getHash(ts)

        val url: HttpUrl = original.url().newBuilder()
            .addQueryParameter(TS_QUERY_PARAM, ts.toString())
            .addQueryParameter(KEY_QUERY_PARAM, PUBLIC_KEY)
            .addQueryParameter(HASH_QUERY_PARAM, hash)
            .build()

        val request = original.newBuilder()
            .url(url)
            .header(ACCEPT_HEADER, ACCEPT_HEADER_VALUE)
            .method(original.method(), original.body())
            .build()

        Timber.d("Request: ${request.method()} ${request.url()}")
        Timber.d("Headers: ${request.headers()}")
        return chain.proceed(request)
    }
}