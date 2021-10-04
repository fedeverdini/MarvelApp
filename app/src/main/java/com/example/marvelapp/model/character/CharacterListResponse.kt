package com.example.marvelapp.model.character

import com.google.gson.annotations.SerializedName

data class CharacterListResponse (
    @SerializedName("code") val code : Int?,
    @SerializedName("status") val status : String?,
    @SerializedName("copyright") val copyright : String?,
    @SerializedName("attributionText") val attributionText : String?,
    @SerializedName("attributionHTML") val attributionHTML : String?,
    @SerializedName("data") val data : Data?,
    @SerializedName("etag") val etag : String?
)