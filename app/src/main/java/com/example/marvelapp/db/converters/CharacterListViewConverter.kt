package com.example.marvelapp.db.converters

import androidx.room.TypeConverter
import com.example.marvelapp.ui.character.list.view.CharacterListView
import com.example.marvelapp.ui.character.list.view.CharacterView
import com.google.gson.Gson
import java.util.*

class CharacterListViewConverter {
    @TypeConverter
    fun fromCharacterListViewtoJson(value: CharacterListView?): String? {
        return Gson().toJson(value, CharacterListView::class.java)
    }

    @TypeConverter
    fun fromJsonToCharacterListView(json: String?): CharacterListView {
        return Gson().fromJson(json, CharacterListView::class.java)
    }

    @TypeConverter
    fun fromListCharacterViewToJson(value: List<CharacterView>?): String = Gson().toJson(value)

    @TypeConverter
    fun fromJsonToListCharacterView(value: String) = Gson().fromJson(value, Array<CharacterView>::class.java).toList()

    @TypeConverter
    fun fromLongtoDate(dateLong: Long?) = dateLong?.let { Date(it) }

    @TypeConverter
    fun fromDateToLong(date: Date?) = date?.time
}