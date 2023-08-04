package com.example.motusgame

import com.example.motusgame.core.utils.networkUtils.ApiResult
import com.example.motusgame.domain.repository.WordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WordRepoFake : WordRepository {
    var isResultSuccessful = false
    override fun getTheWordToGuess(wordLength: Int): Flow<ApiResult<String>> {
        return if (isResultSuccessful) {
            flowOf(
                ApiResult.Success(
                    "working"
                )
            )
        } else
            flowOf(
                ApiResult.Error.ServerError(ApiResult.Error.ServerError.Reason.UNKNOWN)
            )
    }
}