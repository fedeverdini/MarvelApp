package com.example.marvelapp.ui.character.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvelapp.model.DataEvent
import com.example.marvelapp.model.Resource.Status.SUCCESS
import com.example.marvelapp.model.Resource.Status.ERROR
import com.example.marvelapp.usecase.IGetCharacterDetailsUseCase
import com.example.marvelapp.utils.errors.ErrorCode
import com.example.marvelapp.utils.errors.ErrorUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    private val getCharacterDetails: IGetCharacterDetailsUseCase,
    dispatcher: Dispatchers
) : ViewModel() {

    private val ioScope = CoroutineScope(dispatcher.IO)
    private val mainScope = CoroutineScope(dispatcher.Main)

    private val _uiState = MutableLiveData<DataEvent<CharacterDetailsUiState>>()
    val uiState: LiveData<DataEvent<CharacterDetailsUiState>>
        get() = _uiState

    fun getCharacterDetails(characterId: Int) {
        ioScope.launch {
            mainScope.launch {
                _uiState.value = DataEvent(CharacterDetailsUiState.Loading)
            }

            val details = getCharacterDetails.getCharacterDetails(characterId)
            when (details.status) {
                SUCCESS -> {
                    var uiState: CharacterDetailsUiState =
                        CharacterDetailsUiState.ShowError(ErrorUtils.createError(ErrorCode.UNKNOWN))

                    details.data?.let {
                        uiState = CharacterDetailsUiState.ShowCharacterDetails(it)
                    }

                    mainScope.launch {
                        _uiState.value = DataEvent(uiState)
                    }
                }
                ERROR -> {
                    mainScope.launch {
                        _uiState.value = DataEvent(
                            CharacterDetailsUiState.ShowError(
                                details.error ?: ErrorUtils.createError(ErrorCode.UNKNOWN)
                            )
                        )
                    }
                }
            }
        }
    }
}