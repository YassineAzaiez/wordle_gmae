package com.example.motusgame.ui.gameScreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.motusgame.WordRepoFake
import com.example.motusgame.core.utils.networkUtils.ApiResult
import com.example.motusgame.data.repository.MainDispatcherRule
import com.example.motusgame.domain.repository.WordRepository
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())



    private lateinit var wordRepo: WordRepoFake

    private lateinit var viewModel: GameViewModel

    @Before
    fun setUp() {
        wordRepo = WordRepoFake()
        viewModel = GameViewModel(wordRepo)
    }


    @Test
    fun `assert that getTheWordToGuess returns success when api call is successful`() = runTest {
        wordRepo.isResultSuccessful = true
          viewModel.wordLength = 7

        viewModel.getTheWordToGuess()

     val result =  (viewModel.wordToGuessState.first() as ApiResult.Success).data

        Truth.assertThat(result).isEqualTo("working")


    }


    @Test
    fun `assert that getTheWordToGuess returns error when api call fails`() = runTest{
        wordRepo.isResultSuccessful = false
        viewModel.wordLength = 7

        viewModel.getTheWordToGuess()

        val result =  viewModel.wordToGuessError.first()

        Truth.assertThat(result).isEqualTo("something went Wrong")


    }


}