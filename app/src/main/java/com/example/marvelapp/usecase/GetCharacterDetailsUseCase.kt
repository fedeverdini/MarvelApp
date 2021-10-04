package com.example.marvelapp.usecase

import com.example.marvelapp.datasource.IMarvelRepo
import com.example.marvelapp.model.Resource
import com.example.marvelapp.model.Resource.Status.SUCCESS
import com.example.marvelapp.model.Resource.Status.ERROR
import com.example.marvelapp.model.character.Character
import com.example.marvelapp.utils.date.IDateUtils
import com.example.marvelapp.utils.errors.ErrorCode
import com.example.marvelapp.utils.errors.ErrorUtils.Companion.createError
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetCharacterDetailsUseCase(private val marvelRepo: IMarvelRepo) : IGetCharacterDetailsUseCase,
    KoinComponent {

    private val dateUtils: IDateUtils by inject()

    override suspend fun getCharacterDetails(characterId: Int): Resource<Character> {
        val cacheData = marvelRepo.getCharacterDetailsFromCache(characterId)
        return if (cacheData.data == null || dateUtils.isCacheExpired(cacheData.data.lastUpdate)) {
            val response = marvelRepo.getCharacterDetailsFromApi(characterId)
            when (response.status) {
                SUCCESS -> {
                    val character = response.data?.data?.results?.first()
                    character?.let {
                        marvelRepo.saveCharacterDetailsInCache(it)
                        Resource.success(it)
                    } ?: run {
                        // This should never happen
                        Resource.error(createError(ErrorCode.UNKNOWN))
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
}