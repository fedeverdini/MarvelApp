package com.example.marvelapp.usecase

import com.example.marvelapp.ui.character.list.view.CharacterListView

interface IGetCharacterListUseCase {
    suspend fun getCharacterList(page: Int, pullToRefresh: Boolean = false): CharacterListView
}