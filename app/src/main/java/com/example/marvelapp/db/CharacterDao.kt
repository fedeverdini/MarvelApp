package com.example.marvelapp.db

import androidx.room.*
import com.example.marvelapp.model.character.Character

@Dao
interface CharacterDao {
        @Query("SELECT * FROM character WHERE id = :id LIMIT 1")
        fun getCharacter(id: Int): Character

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(vararg character: Character)

        @Delete
        fun delete(user: Character)

        @Query("DELETE FROM character WHERE lastUpdate < :limit")
        fun deleteOldCharacters(limit: Long)

        @Query("DELETE FROM character")
        fun deleteAllCharacters()
}