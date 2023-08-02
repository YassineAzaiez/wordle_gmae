package com.example.motusgame.ui

import android.os.Bundle
import android.util.Log
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
import com.example.motusgame.core.utils.show
import com.example.motusgame.databinding.FragmentGameBinding
import com.example.motusgame.ui.model.CharacterInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameFragment : Fragment() {


    private var _binding: FragmentGameBinding? = null
    private val binding
        get() = _binding!!

    private  val motusGameAdapter: MotusGameAdapter = MotusGameAdapter(mutableListOf())

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
        viewModel.getTheWordToGuess(6)
    }

    private fun initViews() {
        startGame()
        initObservers()
        with(binding) {
            submitButton.setOnClickListener {

                 viewModel.checkUserInput(guessEditText.text.toString())

            }
        }

    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.wordToGuessError.collectLatest { error ->
                        Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
                    }
                }

                launch {
                    viewModel.wordToGuessState.filter { it.isNotEmpty() }.collect { word ->
                        Toast.makeText(requireActivity(), word, Toast.LENGTH_SHORT).show()
                    }
                }

                launch {
                    viewModel.enteredWordsState.filter { it.isNotEmpty() }.collect { word ->
                           binding.rvGuessedWords.apply {
                               adapter = motusGameAdapter
                               layoutManager = GridLayoutManager(requireContext(),6)

                           }
                        motusGameAdapter.addToList(word)
                    }
                }
            }
        }
    }


    private fun showErrorMessage() {
        binding.tvErrorMsg.show()
    }

//    private fun handleGuess(guessedWord: String) {
//        if (guessedWord.isBlank()) return
//
//        if (guessedWord == binding.guessEditText.text.toString()) {
//            showResult("Congratulations! You guessed the word: $selectedWord")
//        } else {
//            val (exactMatches, partialMatches) = evaluateGuess(selectedWord, guessedWord)
//            highlightMatches(exactMatches, partialMatches)
//            attempts++
//
//            if (attempts == maxAttempts) {
//                showResult("You ran out of attempts. The word was: $selectedWord")
//            }
//        }
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}