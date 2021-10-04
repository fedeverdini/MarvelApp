package com.example.marvelapp.ui.character.list.view

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterView(
    @PrimaryKey val id: Int?,
    val name: String?,
    val imageUrl: String?,
    val imageExt: String?
)