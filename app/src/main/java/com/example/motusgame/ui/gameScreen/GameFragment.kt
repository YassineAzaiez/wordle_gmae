package com.example.motusgame.ui.gameScreen

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.motusgame.R
import com.example.motusgame.core.utils.hide
import com.example.motusgame.core.utils.networkUtils.ApiResult
import com.example.motusgame.core.utils.show
import com.example.motusgame.databinding.FragmentGameBinding
import com.example.motusgame.ui.resultScreen.ResultFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch


@AndroidEntryPoint
class GameFragment : Fragment() {


    private var _binding: FragmentGameBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var motusGameAdapter: MotusGameAdapter

    private val viewModel by viewModels<GameViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(layoutInflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun startGame() {
        viewModel.getTheWordToGuess()
    }


    private fun initViews() {
        initObservers()
        initClickListeners()

    }


    private fun initClickListeners() {
        with(binding) {
            submitButton.setOnClickListener {
                if (guessEditText.text.toString().length >= viewModel.wordLength) {
                    viewModel.checkUserInput(guessEditText.text.toString())
                    guessEditText.text.clear()
                } else
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.invalid_word_error),
                        Toast.LENGTH_SHORT
                    ).show()
            }

            btnStartGame.setOnClickListener {
                viewModel.apply {
                    maxAttempts =
                        binding.etNumberOfAttempts.text.toString().takeIf { it.isNotEmpty() }
                            ?.toInt() ?: 0
                    wordLength =
                        binding.etWordSize.text.toString().takeIf { it.isNotEmpty() }?.toInt() ?: 0
                }

                if (viewModel.checkGameSettings()) {

                    toggleSettingsInputs(false)
                    setUpGameMatrixDimension()
                    startGame()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "please check your inputs",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun toggleGameMatrix(isShown: Boolean = false) {
        if (isShown) {
            binding.gameMatrix.show()
        } else {
            binding.gameMatrix.hide()
        }
    }

    private fun toggleSettingsInputs(isShown: Boolean = true) {
        if (isShown) {
            binding.gameSettings.show()
        } else {
            binding.gameSettings.hide()
        }
    }

    private fun setUpGameMatrixDimension() {
        binding.guessEditText.filters = arrayOf(InputFilter.LengthFilter(viewModel.wordLength))
        binding.rvGuessedWords.apply {
            motusGameAdapter = MotusGameAdapter(mutableListOf())
            adapter = motusGameAdapter
            layoutManager = GridLayoutManager(requireContext(), viewModel.wordLength)

        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.wordToGuessError.filter { it.isNotEmpty() }.collect { error ->
                        Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
                    }
                }

                launch {
                    viewModel.wordToGuessState.collectLatest { state ->
                      if(state !is ApiResult.Loading)   toggleGameMatrix(true)
                    }
                }

                launch {
                    viewModel.enteredWordsState.filter { it.isNotEmpty() }.collectLatest { word ->
                        motusGameAdapter.addToList(word)
                    }

                }

                launch {
                    viewModel.gameResultState.collect { result ->
                        clearGameSettingsFields()
                        viewModel.resetAttempts()
                        navigateToResultScreen(result)
                    }
                }
            }
        }
    }

    private fun clearGameSettingsFields(){
        with (binding){
            etWordSize.text.clear()
            etNumberOfAttempts.text.clear()
        }
    }


    private fun navigateToResultScreen(result : Boolean){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.frame_content, ResultFragment.createInstance(result)
            ).commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}