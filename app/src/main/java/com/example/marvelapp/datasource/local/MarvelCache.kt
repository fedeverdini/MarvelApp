package com.example.marvelapp.datasource.local

import com.example.marvelapp.db.CharacterDao
import com.example.marvelapp.db.CharacterListViewDao
import com.example.marvelapp.model.Resource
import com.example.marvelapp.model.character.Character
import com.example.marvelapp.ui.character.list.view.CharacterListView
import com.example.marvelapp.utils.constants.Constants
import timber.log.Timber
import java.lang.Exception
import java.util.*

class MarvelCache(
    private val characterListViewDao: CharacterListViewDao,
    private val characterDao: CharacterDao
) : IMarvelCache {
    override fun getCharacterList(page: Int): Resource<CharacterListView> {
        return try {
            Resource.success(characterListViewDao.getCharacterList(page))
        } catch (e: Exception) {
            Resource.success(null)
        }
    }

    override fun getCharacterDetails(characterId: Int): Resource<Character> {
        return try {
            Resource.success(characterDao.getCharacter(characterId))
        } catch (e: Exception) {
            Resource.success(null)
        }
    }

    override fun saveCharacterList(page: Int, data: CharacterListView) {
        try {
            val now = Date().time
            data.page = page
            data.lastUpdate = now
            characterListViewDao.insert(data)
            characterDao.deleteOldCharacters(now - Constants.CACHE_DELETE_TIME)
        } catch (e: Exception) {
            Timber.d("CACHE SAVE FAILED: saveCharacterList, page: $page")
        }
    }

    override fun saveCharacterDetails(data: Character) {
        try {
            val now = Date().time
            data.lastUpdate = now
            characterDao.insert(data)
            characterDao.deleteOldCharacters(now - Constants.CACHE_DELETE_TIME)
        } catch (e: Exception) {
            Timber.d("CACHE SAVE FAILED: saveCharacterDetails, characterId: ${data.id}")
        }
    }
}