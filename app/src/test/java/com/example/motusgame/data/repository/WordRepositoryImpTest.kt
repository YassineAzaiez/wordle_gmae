package com.example.motusgame.data.repository

import com.example.motusgame.core.utils.networkUtils.ApiResult
import com.example.motusgame.data.dataSource.WordDataSource
import com.example.motusgame.domain.repository.WordRepository
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule constructor(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class WordRepositoryImpTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var wordDataSource: WordDataSource

    private lateinit var wordRepo: WordRepository


    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        wordRepo = WordRepositoryImp(
            wordDataSource, mainDispatcherRule.testDispatcher
        )

    }


    @Test
    fun `assert that getTheWordToGuess returns success result`() = runTest {
        coEvery {
            wordDataSource.getTheWordToGuess(6)
        } returns ApiResult.Success(listOf("working"))


        val result = wordRepo.getTheWordToGuess(6).toList().last()


        coVerify(exactly = 1) { wordDataSource.getTheWordToGuess(6) }

        Truth.assertThat((result as ApiResult.Success).data).isEqualTo("working")


    }


    @Test
    fun `assert that getTheWordToGuess returns error  result when api fails`() = runTest {
        coEvery {
            wordDataSource.getTheWordToGuess(6)
        } returns ApiResult.Error.ServerError(ApiResult.Error.ServerError.Reason.UNKNOWN)


        val result = wordRepo.getTheWordToGuess(6).toList().last()


        coVerify(exactly = 1) { wordDataSource.getTheWordToGuess(6) }

        Truth.assertThat((result as ApiResult.Error.ServerError).reason)
            .isEqualTo(ApiResult.Error.ServerError.Reason.UNKNOWN)


    }


}