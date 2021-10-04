package com.example.marvelapp.usecase

import com.example.marvelapp.model.Resource
import com.example.marvelapp.model.character.Character

interface IGetCharacterDetailsUseCase {
    suspend fun getCharacterDetails(characterId: Int): Resource<Character>
}