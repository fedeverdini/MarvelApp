package com.example.marvelapp.network.retrofit.adapter

import com.example.marvelapp.model.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResourceCall<T>(proxy: Call<T>): CallDelegate<T, Resource<T>>(proxy) {
    override fun enqueueImpl(callback: Callback<Resource<T>>) = proxy.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            callback.onResponse(this@ResourceCall, Response.success(Resource.create(response)))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            callback.onResponse(this@ResourceCall, Response.success(Resource.create(t)))
        }
    })

    override fun cloneImpl(): Call<Resource<T>> = ResourceCall(proxy.clone())
}