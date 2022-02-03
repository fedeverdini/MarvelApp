package com.example.marvelapp.usecase

import com.example.marvelapp.datasource.IMarvelRepo
import com.example.marvelapp.ui.character.list.view.CharacterListView

class GetCharacterListUseCase(private val marvelRepo: IMarvelRepo) : IGetCharacterListUseCase {

    override suspend fun getCharacterList(page: Int, pullToRefresh: Boolean): CharacterListView =
        marvelRepo.getCharacterList(pullToRefresh, page)
}
