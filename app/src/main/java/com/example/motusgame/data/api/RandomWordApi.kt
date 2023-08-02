package com.example.motusgame.data.api

import com.example.motusgame.core.GET_WORD_ROUTE
import com.example.motusgame.core.WORD_LENGTH
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.Flow

interface RandomWordApi {


    @GET(GET_WORD_ROUTE)
   suspend fun getTheWordToGuess(
        @Query(WORD_LENGTH) length : Int
    ) : Response<List<String>>
}