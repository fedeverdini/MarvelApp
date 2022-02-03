package com.example.marvelapp.datasource

import com.example.marvelapp.model.Resource
import com.example.marvelapp.datasource.local.IMarvelCache
import com.example.marvelapp.datasource.remote.MarvelApi
import com.example.marvelapp.model.character.Character
import com.example.marvelapp.model.character.CharacterListMapper
import com.example.marvelapp.ui.character.list.view.CharacterListView
import com.example.marvelapp.utils.date.IDateUtils
import com.example.marvelapp.utils.errors.ErrorCode
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException

class MarvelRepo(
    private val marvelApi: MarvelApi,
    private val marvelCache: IMarvelCache
) : IMarvelRepo, KoinComponent {

    private val dateUtils: IDateUtils by inject()
    private val characterListMapper: CharacterListMapper by inject()

    override suspend fun getCharacterList(pullToRefresh: Boolean, page: Int, limit: Int): CharacterListView {
        val offset = page * limit

        val cacheData = if (pullToRefresh) null else marvelCache.getCharacterList(page)
        if (cacheData == null || dateUtils.isCacheExpired(cacheData.lastUpdate)) {
            val response = marvelApi.getCharacterList(offset, limit)

            when (response.status) {
                Resource.Status.SUCCESS -> {
                    val characterListView = characterListMapper.mapToCharacterListView(response.data)
                    marvelCache.saveCharacterList(page, characterListView)
                    return characterListView
                }
                Resource.Status.ERROR -> {
                    throw IOException(
                        response.error?.errorMessageString ?: ErrorCode.UNKNOWN.message
                    )
                }
            }
        }

        return cacheData
    }

    override suspend fun getCharacterDetails(characterId: Int): Character {
        val cacheData = marvelCache.getCharacterDetails(characterId)
        if (cacheData == null || dateUtils.isCacheExpired(cacheData.lastUpdate)) {
            val response = marvelApi.getCharacterDetails(characterId)

            when (response.status) {
                Resource.Status.SUCCESS -> {
                    val character = characterListMapper.mapToCharacter(response.data)
                    marvelCache.saveCharacterDetails(character)
                    return character
                }
                Resource.Status.ERROR -> {
                    throw IOException(
                        response.error?.errorMessageString ?: ErrorCode.UNKNOWN.message
                    )
                }
            }
        }

        return cacheData
    }
}
