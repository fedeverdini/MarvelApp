package com.example.marvelapp.network.retrofit.adapter

import com.example.marvelapp.model.Resource
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ResourceAdapter(private val type: Type): CallAdapter<Type, Call<Resource<Type>>> {
    override fun responseType(): Type = type
    override fun adapt(call: Call<Type>): Call<Resource<Type>> = ResourceCall(call)
}