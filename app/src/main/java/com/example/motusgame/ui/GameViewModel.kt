package com.example.motusgame.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motusgame.core.utils.networkUtils.ApiResult
import com.example.motusgame.domain.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val wordRepo : WordRepository
) : ViewModel() {

    private val _wordToGuess = MutableStateFlow("")
    val wordToGuess: StateFlow<String> = _wordToGuess

    private val _wordToGuessError = MutableStateFlow("")
    val wordToGuessError: StateFlow<String> = _wordToGuessError

    fun getTheWordToGuess(wordLength : Int){
        viewModelScope.launch {
            wordRepo.getTheWordToGuess(wordLength).collectLatest {result ->
            when(result){
                is ApiResult.Success -> _wordToGuess.emit(result.data)
                is ApiResult.Error ->_wordToGuessError.emit("something went Wrong")
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