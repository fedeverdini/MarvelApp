package com.example.marvelapp.utils.strings

class StringUtils : IStringUtils {
    override fun createFullImageUrl(url: String?, ext: String?, https: Boolean): String {
        val fullUrl = "$url.$ext"
        if (https && fullUrl.contains("http://")) {
            return fullUrl.replace("http://", "https://")
        }
        return fullUrl
    }
}