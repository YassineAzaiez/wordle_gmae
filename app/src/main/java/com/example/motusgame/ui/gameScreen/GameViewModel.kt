package com.example.motusgame.ui.gameScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motusgame.core.utils.networkUtils.ApiResult
import com.example.motusgame.domain.repository.WordRepository
import com.example.motusgame.ui.CharacterInfo
import com.example.motusgame.ui.CharacterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val wordRepo : WordRepository
) : ViewModel() {


    private val _wordToGuessState = MutableSharedFlow<ApiResult<String>>()
    val wordToGuessState = _wordToGuessState.asSharedFlow()

    private val _wordToGuessError = MutableSharedFlow<String>()
    val wordToGuessError = _wordToGuessError

    private val _enteredWordsState = MutableStateFlow<List<CharacterInfo>>(emptyList())
    val enteredWordsState = _enteredWordsState.asStateFlow()

    private val _gameResultState = MutableSharedFlow<Boolean>()
    val gameResultState = _gameResultState.asSharedFlow()


    private var wordToGuess = ""
    private var currentAttempts = 0

    var maxAttempts = 0
    var wordLength = 0

    fun resetAttempts() {
        currentAttempts = 1
    }

    private fun incrementAttempts() {
        currentAttempts++
    }


    fun checkUserInput(userInput: String) {

        viewModelScope.launch {
            when {
                wordToGuess.isEmpty() -> {
                    getTheWordToGuess()
                    resetAttempts()
                }

                else -> {
                    incrementAttempts()
                    checkCharacterState(userInput)
                    checkUserResult()
                }

            }

        }

    }

    private fun checkUserResult() = viewModelScope.launch {

        val isCorrectWord = _enteredWordsState.value.all { it.state == CharacterState.CORRECT }
        when {
            currentAttempts < maxAttempts && isCorrectWord -> _gameResultState.emit(true)
            currentAttempts == maxAttempts -> _gameResultState.emit(isCorrectWord)
        }
    }


    private fun checkCharacterState(userInput: String) {
        viewModelScope.launch {
            if (userInput == wordToGuess) {
                _enteredWordsState.value =
                    userInput.toCharArray().map { CharacterInfo(it, CharacterState.CORRECT) }
            } else {
                val listOfCharacter = mutableListOf<CharacterInfo>()
                userInput.toCharArray().forEachIndexed { index, char ->
                    if (wordToGuess[index] == char) listOfCharacter.add(
                        CharacterInfo(
                            char,
                            CharacterState.CORRECT
                        )
                    )
                    else if (wordToGuess.contains(char, ignoreCase = true)) listOfCharacter.add(
                        CharacterInfo(char, CharacterState.MAL_PLACED)
                    )
                    else listOfCharacter.add(CharacterInfo(char, CharacterState.INCORRECT))
                }
                _enteredWordsState.value = listOfCharacter
            }
        }
    }


    fun getTheWordToGuess() {
        viewModelScope.launch {
            wordRepo.getTheWordToGuess(wordLength).collectLatest { result ->
                when (result) {
                    is ApiResult.Success -> {
                        wordToGuess = result.data
                        _wordToGuessState.emit(result)
                    }

                    is ApiResult.Error ->
                        _wordToGuessError.emit("something went Wrong")

                    else -> {
                        _wordToGuessState.emit(value = result)
                    }
                }

            }
        }
    }

    fun checkGameSettings(): Boolean {
        return when {
            maxAttempts == 0 || wordLength == 0 -> false
            wordLength !in 6..9 -> false
            else -> true
        }

    }

}