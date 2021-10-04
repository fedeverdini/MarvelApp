package com.example.marvelapp.model.character

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class Character (
    // important for cache
    var lastUpdate: Long,

    @PrimaryKey
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("description") val description : String,
    @SerializedName("modified") val modified : Date,
    @SerializedName("resourceURI") val resourceURI : String,
    @SerializedName("urls") val urls : List<Urls>,
    @SerializedName("thumbnail") val thumbnail : Thumbnail,
    @SerializedName("comics") val comics : Comics,
    @SerializedName("stories") val stories : Stories,
    @SerializedName("events") val events : Events,
    @SerializedName("series") val series : Series
)