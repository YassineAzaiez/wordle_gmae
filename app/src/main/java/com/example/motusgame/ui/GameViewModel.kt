package com.example.motusgame.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motusgame.core.utils.networkUtils.ApiResult
import com.example.motusgame.domain.repository.WordRepository
import com.example.motusgame.ui.model.CharacterInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val wordRepo : WordRepository
) : ViewModel() {

    private val _wordToGuessState = MutableStateFlow("")
    val wordToGuessState: StateFlow<String> = _wordToGuessState

    private val _enteredWordsState = MutableStateFlow<List<CharacterInfo>>(emptyList())
    val enteredWordsState: StateFlow<List<CharacterInfo>> = _enteredWordsState

    private var wordToGuess = ""
    var maxAttempts = 5


    fun checkUserInput(userInput: String) {
        viewModelScope.launch {
            if (userInput == wordToGuess) {
                _enteredWordsState.emit(
                    userInput.toCharArray().map { CharacterInfo(it, CharacterState.CORRECT) })
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
                _enteredWordsState.emit(listOfCharacter)
            }
        }

    }

    private val _wordToGuessError = MutableStateFlow("")
    val wordToGuessError: StateFlow<String> = _wordToGuessError

    fun getTheWordToGuess(wordLength: Int) {
        viewModelScope.launch {
            wordRepo.getTheWordToGuess(wordLength).collectLatest { result ->
                when (result) {
                    is ApiResult.Success -> {
                        _wordToGuessState.emit(result.data)
                        wordToGuess = result.data
                    }

                    is ApiResult.Error -> _wordToGuessError.emit("something went Wrong")
                    else -> {
                        //loading state
                    }
                }

            }
        }
    }


    fun checkGuessedWord(guessedWord: String, currentWord : String): GuessWordValidation = when {
        guessedWord.isEmpty() -> GuessWordValidation.EMPTY
        guessedWord.length > currentWord.length -> GuessWordValidation.LONG
        else -> GuessWordValidation.VALID
    }


}