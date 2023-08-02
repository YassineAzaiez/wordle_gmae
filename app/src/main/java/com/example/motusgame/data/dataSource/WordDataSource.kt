package com.example.motusgame.data.dataSource

import com.example.motusgame.core.utils.networkUtils.ApiResult

interface WordDataSource {

    suspend fun getTheWordToGuess(wordLength : Int) : ApiResult<List<String>>
}