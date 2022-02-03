package com.example.marvelapp.datasource

import com.example.marvelapp.model.character.Character
import com.example.marvelapp.ui.character.list.view.CharacterListView
import com.example.marvelapp.utils.constants.Constants

interface IMarvelRepo {
    suspend fun getCharacterList(
        pullToRefresh: Boolean,
        page: Int,
        limit: Int = Constants.CHARACTER_LIST_PARAM_LIMIT
    ): CharacterListView

    suspend fun getCharacterDetails(characterId: Int): Character
}
