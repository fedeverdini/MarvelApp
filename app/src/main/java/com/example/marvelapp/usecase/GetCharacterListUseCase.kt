package com.example.marvelapp.usecase

import com.example.marvelapp.datasource.IMarvelRepo
import com.example.marvelapp.model.Resource
import com.example.marvelapp.model.Resource.Status.SUCCESS
import com.example.marvelapp.model.Resource.Status.ERROR
import com.example.marvelapp.ui.character.list.view.CharacterListView
import com.example.marvelapp.ui.character.list.view.CharacterView
import com.example.marvelapp.utils.constants.Constants
import com.example.marvelapp.utils.date.IDateUtils
import com.example.marvelapp.utils.errors.ErrorCode
import com.example.marvelapp.utils.errors.ErrorUtils.Companion.createError
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetCharacterListUseCase(private val marvelRepo: IMarvelRepo) : IGetCharacterListUseCase,
    KoinComponent {
    private val dateUtils: IDateUtils by inject()

    override suspend fun getCharacterList(
        page: Int,
        pullToRefresh: Boolean
    ): Resource<CharacterListView> {
        val cacheData = if (pullToRefresh) null else marvelRepo.getCharacterListFromCache(page)
        return if (cacheData?.data == null || dateUtils.isCacheExpired(cacheData.data.lastUpdate)) {
            val response = marvelRepo.getCharacterListFromApi(page)
            when (response.status) {
                SUCCESS -> {
                    val charList = response.data?.data?.results?.map { character ->
                        CharacterView(
                            character.id,
                            character.name,
                            character.thumbnail.path,
                            character.thumbnail.extension
                        )
                    }

                    if (charList.isNullOrEmpty()) {
                        Resource.success(CharacterListView(characterList = emptyList()))
                    } else {
                        val charListView = CharacterListView(
                            characterList = charList,
                            totalPages = getTotalPages(response.data.data.total)
                        )
                        marvelRepo.saveCharacterListInCache(page, charListView)
                        Resource.success(charListView)
                    }
                }
                ERROR -> {
                    Resource.error(response.error ?: createError(ErrorCode.UNKNOWN))
                }
            }
        } else {
            Resource.success(cacheData.data)
        }
    }

    private fun getTotalPages(totalCharacters: Int?): Int {
        return totalCharacters?.let {
            it / Constants.CHARACTER_LIST_PARAM_LIMIT
        } ?: 0
    }
}