package com.example.motusgame.data.dataSource

import com.example.motusgame.core.utils.networkUtils.ApiResult
import com.example.motusgame.core.utils.networkUtils.ApiResultMapper.toApiResult
import com.example.motusgame.data.api.RandomWordApi

class WordDataSourceImp(
    private val api: RandomWordApi
) : WordDataSource{
    override suspend fun getTheWordToGuess(wordLength: Int): ApiResult<List<String>> =
        toApiResult {
            api.getTheWordToGuess(wordLength)
        }


}