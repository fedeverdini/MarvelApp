package com.example.marvelapp.network.retrofit.adapter

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class CallDelegate<Tin, Tout>(protected val proxy: Call<Tin>) : Call<Tout> {
    override fun execute(): Response<Tout> = throw NotImplementedError()
    override fun enqueue(callback: Callback<Tout>) = enqueueImpl(callback)
    override fun clone(): Call<Tout> = cloneImpl()

    override fun cancel() = proxy.cancel()
    override fun request(): Request = proxy.request()
    override fun isExecuted() = proxy.isExecuted
    override fun isCanceled() = proxy.isCanceled
    override fun timeout(): Timeout = proxy.timeout()

    abstract fun enqueueImpl(callback: Callback<Tout>)
    abstract fun cloneImpl(): Call<Tout>
}