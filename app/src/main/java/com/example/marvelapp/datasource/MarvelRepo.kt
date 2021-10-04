package com.example.marvelapp.datasource

import com.example.marvelapp.model.Resource
import com.example.marvelapp.datasource.local.IMarvelCache
import com.example.marvelapp.datasource.remote.MarvelApi
import com.example.marvelapp.model.character.Character
import com.example.marvelapp.model.character.CharacterListResponse
import com.example.marvelapp.ui.character.list.view.CharacterListView

class MarvelRepo(
    private val marvelApi: MarvelApi,
    private val marvelCache: IMarvelCache
) : IMarvelRepo {

    override suspend fun getCharacterListFromCache(page: Int): Resource<CharacterListView> {
        return marvelCache.getCharacterList(page)
    }

    override suspend fun getCharacterDetailsFromCache(characterId: Int): Resource<Character> {
        return marvelCache.getCharacterDetails(characterId)
    }

    override suspend fun getCharacterListFromApi(page: Int, limit: Int): Resource<CharacterListResponse> {
        val offset = page * limit
        return marvelApi.getCharacterList(offset, limit)
    }

    override suspend fun getCharacterDetailsFromApi(characterId: Int): Resource<CharacterListResponse> {
        return marvelApi.getCharacterDetails(characterId)
    }

    override fun saveCharacterListInCache(page: Int, characterListView: CharacterListView) {
        marvelCache.saveCharacterList(page, characterListView)
    }

    override fun saveCharacterDetailsInCache(characterDetails: Character) {
        marvelCache.saveCharacterDetails(characterDetails)
    }
}