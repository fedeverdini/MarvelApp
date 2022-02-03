package com.example.marvelapp.datasource.local

import com.example.marvelapp.db.CharacterDao
import com.example.marvelapp.db.CharacterListViewDao
import com.example.marvelapp.model.character.Character
import com.example.marvelapp.ui.character.list.view.CharacterListView
import com.example.marvelapp.utils.constants.Constants
import com.example.marvelapp.utils.errors.ErrorCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.util.*

class MarvelCache(
    private val characterListViewDao: CharacterListViewDao,
    private val characterDao: CharacterDao
) : IMarvelCache {

    override fun getCharacterList(page: Int): CharacterListView? {
        return try {
            characterListViewDao.getCharacterList(page)
        } catch (e: Exception) {
            null
        }
    }

    override fun getCharacterDetails(characterId: Int): Character? {
        return try {
            characterDao.getCharacter(characterId)
        } catch (e: Exception) {
            null
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