package com.example.marvelapp.utils.encrypt

interface IEncryptUtils {
    fun getHash(ts: Long): String
}