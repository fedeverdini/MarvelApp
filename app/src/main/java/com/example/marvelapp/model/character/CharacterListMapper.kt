package com.example.marvelapp.model.character

import com.example.marvelapp.ui.character.list.view.CharacterListView
import com.example.marvelapp.ui.character.list.view.CharacterView
import com.example.marvelapp.utils.constants.Constants
import com.example.marvelapp.utils.errors.ErrorCode
import java.lang.Exception

class CharacterListMapper {
    fun mapToCharacter(list: CharacterListResponse?): Character {
        if (list?.data != null && list.data.count > 0) {
            return list.data.results.first()
        } else {
            throw Exception(ErrorCode.UNKNOWN.message)
        }
    }

    fun mapToCharacterListView(list: CharacterListResponse?): CharacterListView {
        val charList = list?.data?.results?.map { character ->
            CharacterView(
                character.id,
                character.name,
                character.thumbnail.path,
                character.thumbnail.extension
            )
        }

        return if (charList.isNullOrEmpty()) {
            CharacterListView(characterList = emptyList())
        } else {
            CharacterListView(
                characterList = charList,
                totalPages = getTotalPages(list.data.total)
            )
        }
    }

    private fun getTotalPages(totalCharacters: Int?): Int {
        return totalCharacters?.let {
            it / Constants.CHARACTER_LIST_PARAM_LIMIT
        } ?: 0
    }
}