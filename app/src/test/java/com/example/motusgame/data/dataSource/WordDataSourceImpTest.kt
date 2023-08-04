package com.example.motusgame.data.dataSource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.motusgame.BuildConfig
import com.example.motusgame.core.utils.networkUtils.ApiResult
import com.example.motusgame.data.api.RandomWordApi
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.HttpUrl
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("IMPLICIT_CAST_TO_ANY")
@OptIn(ExperimentalCoroutinesApi::class)
class WordDataSourceImpTest {
    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    private val mockServer: MockWebServer = MockWebServer()
    private lateinit var dataSourceImp: WordDataSourceImp
    private lateinit var wordApiService: RandomWordApi

    private lateinit var baseUrl: HttpUrl

    @Before
    fun setUp() {
        mockServer.start()
        baseUrl = mockServer.url("/")
        mockServer.dispatcher = setUpMockWebServerDispatcher()
        setUpMarvelRetrofitService()
        dataSourceImp = WordDataSourceImp(wordApiService)
    }


    @After
    fun tearDown() {
        mockServer.shutdown()
    }


    @Test
    fun `Assert getTheWordToGuess  returns valid api response`() = runTest {
        val word = when (val result = dataSourceImp.getTheWordToGuess(6)) {
            is ApiResult.Success -> result.data.first()
            else -> ""
        }

        Truth.assertThat(word.length).isEqualTo(6)
        Truth.assertThat(word).isEqualTo("vealer")
    }


    @Test
    fun `Assert getTheWordToGuess  returns error when api fails `() = runTest {
        val word = when (val result = dataSourceImp.getTheWordToGuess(9)) {
            is ApiResult.Error.ServerError -> result.reason
            else -> {}
        }


        Truth.assertThat(word).isEqualTo(ApiResult.Error.ServerError.Reason.FORBIDDEN)
    }


    private fun setUpMarvelRetrofitService() {
        wordApiService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(mockServer.url("/"))
            .build()
            .create(RandomWordApi::class.java)
    }


    private fun setUpMockWebServerDispatcher(): Dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            println("${BuildConfig.BASE_URL}${request.path}")
            return when (request.path) {
                "/word?length=6" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(
                            """[
                                   "vealer"
                               ]""".trimIndent()
                        )
                }

                "/word?length=7" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(
                            """[
                                   clunker
                               ]""".trimIndent()
                        )
                }

                "/word?length=8" -> {
                    MockResponse()
                        .setResponseCode(200)
                        .setBody(
                            """[
                                    "revivify"
                               ]""".trimIndent()
                        )
                }

                "/word?length=9" -> {
                    MockResponse()
                        .setResponseCode(403)
                                        }

                else -> MockResponse().setResponseCode(404)
            }
        }
    }
}