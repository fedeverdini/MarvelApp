package com.example.marvelapp.ui.character.list

import com.example.marvelapp.model.error.BaseError
import com.example.marvelapp.ui.character.list.view.CharacterListView

sealed class CharacterListUiState {
    object Loading: CharacterListUiState()
    data class ShowCharacterList(val result: CharacterListView): CharacterListUiState()
    object ShowEmptyList: CharacterListUiState()
    data class ShowError(val error: BaseError): CharacterListUiState()
}