package com.example.motusgame.domain.repository

import com.example.motusgame.core.utils.networkUtils.ApiResult
import kotlinx.coroutines.flow.Flow

interface WordRepository {
    fun getTheWordToGuess( wordLength : Int) : Flow<ApiResult<String>>
}