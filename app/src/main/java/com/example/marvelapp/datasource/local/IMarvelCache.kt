package com.example.marvelapp.datasource.local

import com.example.marvelapp.model.Resource
import com.example.marvelapp.model.character.Character
import com.example.marvelapp.ui.character.list.view.CharacterListView

interface IMarvelCache {
    fun getCharacterList(page: Int): Resource<CharacterListView>
    fun getCharacterDetails(characterId: Int): Resource<Character>

    fun saveCharacterList(page: Int, data: CharacterListView)
    fun saveCharacterDetails(data: Character)
}