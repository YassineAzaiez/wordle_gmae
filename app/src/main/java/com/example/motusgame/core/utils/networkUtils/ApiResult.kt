package com.example.motusgame.core.utils.networkUtils

sealed class ApiResult<out R> {

    data class Success<R>(val data: R) : ApiResult<R>()
    object Loading : ApiResult<Nothing>()

    sealed class Error : ApiResult<Nothing>() {

        data class ServerError(val reason: Reason) : Error() {

            enum class Reason {
                UNAUTHORIZED,
                BAD_REQUEST,
                FORBIDDEN,
                SERVICE_UNAVAILABLE,
                SERVER_UNREACHABLE,
                UNKNOWN
            }
        }

    }

}
