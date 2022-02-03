package com.example.marvelapp.usecase

import com.example.marvelapp.datasource.IMarvelRepo

class GetCharacterDetailsUseCase(private val marvelRepo: IMarvelRepo) :
    IGetCharacterDetailsUseCase {
    override suspend fun getCharacterDetails(characterId: Int) =
        marvelRepo.getCharacterDetails(characterId)
}
