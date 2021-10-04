package com.example.marvelapp.ui.character.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvelapp.model.DataEvent
import com.example.marvelapp.model.Resource.Status.SUCCESS
import com.example.marvelapp.model.Resource.Status.ERROR
import com.example.marvelapp.usecase.IGetCharacterListUseCase
import com.example.marvelapp.utils.errors.ErrorCode
import com.example.marvelapp.utils.errors.ErrorUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val getCharacterList: IGetCharacterListUseCase,
    dispatcher: Dispatchers
) : ViewModel() {

    private val ioScope = CoroutineScope(dispatcher.IO)
    private val mainScope = CoroutineScope(dispatcher.Main)

    private val _uiState = MutableLiveData<DataEvent<CharacterListUiState>>()
    val uiState: LiveData<DataEvent<CharacterListUiState>>
        get() = _uiState

    fun getCharacterList(page: Int = 0, pullToRefresh: Boolean = false) {
        ioScope.launch {
            mainScope.launch {
                _uiState.value = DataEvent(CharacterListUiState.Loading)
            }

            val list = getCharacterList.getCharacterList(page, pullToRefresh)
            when (list.status) {
                SUCCESS -> {
                    var uiState: CharacterListUiState = CharacterListUiState.ShowEmptyList

                    list.data?.characterList?.let { characterList ->
                        if (characterList.isNotEmpty()) {
                            uiState = CharacterListUiState.ShowCharacterList(list.data)
                        }
                    }

                    mainScope.launch {
                        _uiState.value = DataEvent(uiState)
                    }
                }
                ERROR -> {
                    mainScope.launch {
                        _uiState.value = DataEvent(
                            CharacterListUiState.ShowError(
                                list.error ?: ErrorUtils.createError(ErrorCode.UNKNOWN)
                            )
                        )
                    }
                }
            }
        }
    }
}