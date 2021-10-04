package com.example.marvelapp.model.character

import com.google.gson.annotations.SerializedName

data class Comics (
	@SerializedName("available") override val available : Int,
	@SerializedName("returned") override val returned : Int,
	@SerializedName("collectionURI") override val collectionURI : String,
	@SerializedName("items") override val items : List<Items>
): IDetailObject