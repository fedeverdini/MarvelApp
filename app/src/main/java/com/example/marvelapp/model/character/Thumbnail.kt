package com.example.marvelapp.model.character

import com.google.gson.annotations.SerializedName

data class Thumbnail (
	@SerializedName("path") val path : String,
	@SerializedName("extension") val extension : String
)