package com.example.marvelapp

import com.example.marvelapp.model.character.Character
import com.example.marvelapp.model.error.BaseError
import com.example.marvelapp.ui.character.details.CharacterDetailsUiState
import com.example.marvelapp.ui.character.details.CharacterDetailsViewModel
import com.example.marvelapp.usecase.GetCharacterDetailsUseCase
import com.example.marvelapp.utils.errors.ErrorCode
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

class CharacterDetailsViewModelTest {
    @get: Rule
    val coroutineRule = CoroutinesTestRule()

    private val getCharacterDetailsUseCase = mock<GetCharacterDetailsUseCase>()

    private val sut by lazy {
        CharacterDetailsViewModel(getCharacterDetailsUseCase, coroutineRule.testDispatcherProvider)
    }

    @Before
    fun setCharacterId() {
        sut.setCharacterId(1)
    }

    @Test
    fun `show character details`() = runBlockingTest {
        // Test Data
        val testData = mock<Character>()

        // Given
        stubbing(getCharacterDetailsUseCase) {
            onBlocking { it.getCharacterDetails(any()) } doReturn testData
        }

        val finalExpectedState = CharacterDetailsUiState.ShowCharacterDetails(testData)

        // When
        sut.setCharacterId(1)
        sut.forceRefreshState()

        val actualState = sut.uiState.value

        // Then
        Assert.assertEquals(finalExpectedState, actualState)
    }

    @Test
    fun `show error screen`() = runBlockingTest {
        // Given
        stubbing(getCharacterDetailsUseCase) {
            onBlocking { it.getCharacterDetails(any()) } doThrow (RuntimeException())
        }

        val finalExpectedState = CharacterDetailsUiState.ShowError(
            BaseError(
                ErrorCode.UNKNOWN,
                errorMessageString = "Iron-Man is checking what's happening, please try in a few minutes"
            )
        )

        // When
        sut.setCharacterId(1)
        sut.forceRefreshState()

        val actualState = sut.uiState.value

        // Then
        Assert.assertEquals(finalExpectedState, actualState)
    }
}
