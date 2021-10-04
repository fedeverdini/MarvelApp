package com.example.marvelapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.marvelapp.db.converters.CharacterConverter
import com.example.marvelapp.db.converters.CharacterListViewConverter
import com.example.marvelapp.db.converters.CharacterViewConverter
import com.example.marvelapp.model.character.Character
import com.example.marvelapp.ui.character.list.view.CharacterListView
import com.example.marvelapp.ui.character.list.view.CharacterView

@Database(entities = [CharacterListView::class, CharacterView::class, Character::class], version = 1, exportSchema = false)
@TypeConverters(CharacterListViewConverter::class, CharacterViewConverter::class, CharacterConverter::class)
abstract class MarvelDatabase : RoomDatabase() {
    abstract fun characterListViewDao(): CharacterListViewDao
    abstract fun characterDao(): CharacterDao

    companion object {
        fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, MarvelDatabase::class.java, "MarvelDatabase")
                .fallbackToDestructiveMigration()
                .build()
    }
}