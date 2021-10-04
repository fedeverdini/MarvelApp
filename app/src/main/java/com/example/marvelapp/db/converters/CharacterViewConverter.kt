package com.example.marvelapp.db.converters

import androidx.room.TypeConverter
import com.example.marvelapp.ui.character.list.view.CharacterView
import com.google.gson.Gson

class CharacterViewConverter {
    @TypeConverter
    fun fromCharacterViewtoJson(value: CharacterView?): String? {
        return Gson().toJson(value, CharacterView::class.java)
    }

    @TypeConverter
    fun fromJsonToCharacterView(json: String?): CharacterView {
        return Gson().fromJson(json, CharacterView::class.java)
    }


}