package com.example.marvelapp.ui.character.details

import com.example.marvelapp.model.character.Character
import com.example.marvelapp.model.error.BaseError

sealed class CharacterDetailsUiState {
    object Loading : CharacterDetailsUiState()
    data class ShowCharacterDetails(val result: Character) : CharacterDetailsUiState()
    data class ShowError(val error: BaseError) : CharacterDetailsUiState()
}