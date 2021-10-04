package com.example.marvelapp.ui.character.list.view

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterListView(
    // important for cache
    @PrimaryKey var page: Int? = null,
    var totalPages: Int? = null,
    var lastUpdate: Long? = null,
    val characterList: List<CharacterView>
)