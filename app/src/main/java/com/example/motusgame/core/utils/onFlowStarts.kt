package com.example.motusgame.core.utils

import com.example.motusgame.core.utils.networkUtils.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart


/**
 * extension function for Flow Class to emit loading state before the flow starts
 */
fun <T> Flow<ApiResult<T>>.onFlowStarts() =
    onStart {
        emit(ApiResult.Loading)
    }.catch {
        emit(
            ApiResult.Error.ServerError(
                ApiResult.Error.ServerError.Reason.UNKNOWN
            )
        )
    }
