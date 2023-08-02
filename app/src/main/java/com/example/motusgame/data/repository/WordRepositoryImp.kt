package com.example.motusgame.data.repository

import com.example.motusgame.core.utils.networkUtils.ApiResult
import com.example.motusgame.core.utils.onFlowStarts
import com.example.motusgame.data.dataSource.WordDataSource
import com.example.motusgame.domain.repository.WordRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class WordRepositoryImp(
    private val api: WordDataSource,
    private val dispatcher: CoroutineDispatcher
) : WordRepository {
    override fun getTheWordToGuess(wordLength: Int) = flow {
        api.getTheWordToGuess(wordLength).run {
            when (this) {
                is ApiResult.Success -> emit(ApiResult.Success(data.first()))
                is ApiResult.Error -> emit(this)
                else -> {
                    // loading state is handled using the onFlowStarts() extension function
                }

            }
        }
    }
        .flowOn(dispatcher)
        .onFlowStarts()


}