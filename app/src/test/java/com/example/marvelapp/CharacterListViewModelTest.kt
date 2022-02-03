package com.example.marvelapp

import com.example.marvelapp.model.error.BaseError
import com.example.marvelapp.ui.character.list.CharacterListUiState
import com.example.marvelapp.ui.character.list.CharacterListViewModel
import com.example.marvelapp.ui.character.list.view.CharacterListView
import com.example.marvelapp.ui.character.list.view.CharacterView
import com.example.marvelapp.usecase.GetCharacterListUseCase
import com.example.marvelapp.utils.errors.ErrorCode
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

class CharacterListViewModelTest {
    @get: Rule
    val coroutineRule = CoroutinesTestRule()

    private val getCharacterListUseCase = mock<GetCharacterListUseCase>()

    private val sut by lazy {
        CharacterListViewModel(getCharacterListUseCase, coroutineRule.testDispatcherProvider)
    }

    @Test
    fun `empty character list`() = runBlockingTest {
        // Given
        stubbing(getCharacterListUseCase) {
            onBlocking { it.getCharacterList(any(), any()) } doReturn CharacterListView(
                characterList = emptyList())
        }

        val expectedState = CharacterListUiState.ShowEmptyList

        // When
        val actualState = sut.uiState.value

        // Then
        Assert.assertEquals(expectedState, actualState)
    }

    @Test
    fun `show character list`() = runBlockingTest {
        // Test Data
        val testData =
            CharacterListView(characterList = listOf(CharacterView(1, "Test Name", null, null)))
        // Given
        stubbing(getCharacterListUseCase) {
            onBlocking { it.getCharacterList(any(), any()) } doReturn testData
        }

        val expectedState = CharacterListUiState.ShowCharacterList(testData)

        // When
        sut.forceRefreshState()
        val actualState = sut.uiState.value

        // Then
        Assert.assertEquals(expectedState, actualState)
    }

    @Test
    fun `show error screen`() = runBlockingTest {
        // Given
        stubbing(getCharacterListUseCase) {
            onBlocking { it.getCharacterList(any(), any()) } doThrow (RuntimeException())
        }

        val expectedState = CharacterListUiState.ShowError(BaseError(ErrorCode.UNKNOWN,
            errorMessageString = "Spider-Man is checking what's happening, please try in a few minutes"))

        // When
        val actualState = sut.uiState.value

        // Then
        Assert.assertEquals(expectedState, actualState)
    }
}
