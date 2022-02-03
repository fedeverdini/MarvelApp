package com.example.marvelapp.ui.character.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelapp.core.DefaultDispatcherProvider
import com.example.marvelapp.core.DispatcherProvider
import com.example.marvelapp.usecase.IGetCharacterListUseCase
import com.example.marvelapp.utils.errors.ErrorCode
import com.example.marvelapp.utils.errors.ErrorUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class CharacterListViewModel(
    private val getCharacterList: IGetCharacterListUseCase,
    private val dispatcher: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    private var page: Int = 0
    private var pullToRefresh = false

    fun setPage(value: Int) {
        this.page = value
    }

    fun setPullToRefresh(value: Boolean) {
        this.pullToRefresh = value
    }

    fun forceRefreshState() {
        refreshFlow.tryEmit(Unit)
    }

    private val refreshFlow = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    @ExperimentalCoroutinesApi
    val uiState: StateFlow<CharacterListUiState> =
        refreshFlow.onStart {
            emit(Unit)
        }.flatMapLatest {
            requestState()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = CharacterListUiState.Loading
        )

    private fun requestState() = flow<CharacterListUiState> {
        val state = try {
            val characterListView = getCharacterList.getCharacterList(page, pullToRefresh)
            if (characterListView.characterList.isEmpty()) {
                CharacterListUiState.ShowEmptyList
            } else {
                CharacterListUiState.ShowCharacterList(characterListView)
            }
        } catch (exception: Exception) {
            CharacterListUiState.ShowError(ErrorUtils.createError(ErrorCode.UNKNOWN))
        }

        emit(state)
    }.catch {
        emit(CharacterListUiState.ShowError(ErrorUtils.createError(ErrorCode.UNKNOWN)))
    }.onStart {
        emit(CharacterListUiState.Loading)
    }.flowOn(dispatcher.io())
}
