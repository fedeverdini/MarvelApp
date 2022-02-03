package com.example.marvelapp.ui.character.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelapp.core.DefaultDispatcherProvider
import com.example.marvelapp.core.DispatcherProvider
import com.example.marvelapp.usecase.IGetCharacterDetailsUseCase
import com.example.marvelapp.utils.errors.ErrorCode
import com.example.marvelapp.utils.errors.ErrorUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import java.lang.Exception

class CharacterDetailsViewModel(
    private val getCharacterDetails: IGetCharacterDetailsUseCase,
    private val dispatcher: DispatcherProvider = DefaultDispatcherProvider()
) :
    ViewModel() {

    private var characterId: Int? = null

    fun setCharacterId(characterId: Int) {
        this.characterId = characterId
    }

    private val refreshFlow = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    fun forceRefreshState() {
        refreshFlow.tryEmit(Unit)
    }

    @ExperimentalCoroutinesApi
    val uiState: StateFlow<CharacterDetailsUiState> =
        refreshFlow.onStart {
            emit(Unit)
        }.flatMapLatest {
            requestState()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = CharacterDetailsUiState.Loading
        )

    private fun requestState() = flow<CharacterDetailsUiState> {
        characterId?.let {
            val state = try {
                val characterDetail = getCharacterDetails.getCharacterDetails(it)
                CharacterDetailsUiState.ShowCharacterDetails(characterDetail)
            } catch (exception: Exception) {
                CharacterDetailsUiState.ShowError(ErrorUtils.createError(ErrorCode.UNKNOWN))
            }

            emit(state)
        }
    }.catch {
        emit(CharacterDetailsUiState.ShowError(ErrorUtils.createError(ErrorCode.UNKNOWN)))
    }.onStart {
        emit(CharacterDetailsUiState.Loading)
    }.flowOn(dispatcher.io())
}
