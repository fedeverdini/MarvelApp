package com.example.marvelapp.datasource

import com.example.marvelapp.model.Resource
import com.example.marvelapp.model.character.Character
import com.example.marvelapp.model.character.CharacterListResponse
import com.example.marvelapp.ui.character.list.view.CharacterListView
import com.example.marvelapp.utils.constants.Constants

interface IMarvelRepo {
    suspend fun getCharacterListFromCache(page: Int): Resource<CharacterListView>
    suspend fun getCharacterDetailsFromCache(characterId: Int): Resource<Character>

    suspend fun getCharacterListFromApi(page: Int, limit: Int = Constants.CHARACTER_LIST_PARAM_LIMIT): Resource<CharacterListResponse>
    suspend fun getCharacterDetailsFromApi(characterId: Int): Resource<CharacterListResponse>

    fun saveCharacterListInCache(page: Int, characterListView: CharacterListView)
    fun saveCharacterDetailsInCache(characterDetails: Character)
}