package com.example.marvelapp.db

import androidx.room.*
import com.example.marvelapp.ui.character.list.view.CharacterListView

@Dao
interface CharacterListViewDao {
        @Query("SELECT * FROM characterListView WHERE page = :page")
        fun getCharacterList(page: Int): CharacterListView

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(vararg characters: CharacterListView)

        @Delete
        fun delete(characters: CharacterListView)

        @Query("DELETE FROM CharacterListView WHERE lastUpdate < :limit")
        fun deleteOldPages(limit: Long)

        @Query("DELETE FROM characterListView")
        fun deleteAllPages()
}