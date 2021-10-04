package com.example.marvelapp.db.converters

import androidx.room.TypeConverter
import com.example.marvelapp.model.character.*
import com.google.gson.Gson

class CharacterConverter {
    @TypeConverter
    fun fromCharacterToJson(value: Character?): String? {
        return Gson().toJson(value, Character::class.java)
    }

    @TypeConverter
    fun fromJsonToCharacter(json: String?): Character {
        return Gson().fromJson(json, Character::class.java)
    }

    @TypeConverter
    fun fromUrlsToJson(value: Urls?): String? {
        return Gson().toJson(value, Urls::class.java)
    }

    @TypeConverter
    fun fromJsonToUrls(json: String?): Urls {
        return Gson().fromJson(json, Urls::class.java)
    }

    @TypeConverter
    fun fromThumbnailToJson(value: Thumbnail?): String? {
        return Gson().toJson(value, Thumbnail::class.java)
    }

    @TypeConverter
    fun fromJsonToThumbnail(json: String?): Thumbnail {
        return Gson().fromJson(json, Thumbnail::class.java)
    }

    @TypeConverter
    fun fromComicsToJson(value: Comics?): String? {
        return Gson().toJson(value, Comics::class.java)
    }

    @TypeConverter
    fun fromJsonToComics(json: String?): Comics {
        return Gson().fromJson(json, Comics::class.java)
    }

    @TypeConverter
    fun fromStoriesToJson(value: Stories?): String? {
        return Gson().toJson(value, Stories::class.java)
    }

    @TypeConverter
    fun fromJsonToStories(json: String?): Stories {
        return Gson().fromJson(json, Stories::class.java)
    }

    @TypeConverter
    fun fromEventsToJson(value: Events?): String? {
        return Gson().toJson(value, Events::class.java)
    }

    @TypeConverter
    fun fromJsonToEvents(json: String?): Events {
        return Gson().fromJson(json, Events::class.java)
    }

    @TypeConverter
    fun fromSeriesToJson(value: Series?): String? {
        return Gson().toJson(value, Series::class.java)
    }

    @TypeConverter
    fun fromJsonToSeries(json: String?): Series {
        return Gson().fromJson(json, Series::class.java)
    }

    @TypeConverter
    fun fromListUrlsToJson(value: List<Urls>?): String = Gson().toJson(value)

    @TypeConverter
    fun fromJsonToListUrls(value: String) = Gson().fromJson(value, Array<Urls>::class.java).toList()
}
