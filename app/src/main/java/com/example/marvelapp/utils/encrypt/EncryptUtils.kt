package com.example.marvelapp.utils.encrypt

import java.security.MessageDigest
import com.example.marvelapp.utils.constants.Keys.PUBLIC_KEY
import com.example.marvelapp.utils.constants.Keys.PRIVATE_KEY
import java.math.BigInteger

class EncryptUtils : IEncryptUtils {
    override fun getHash(ts: Long): String {
        val md = MessageDigest.getInstance("MD5")
        val value = "$ts${PRIVATE_KEY}${PUBLIC_KEY}"
        return String.format("%032x", BigInteger(1, md.digest(value.toByteArray(Charsets.UTF_8))))
    }
}