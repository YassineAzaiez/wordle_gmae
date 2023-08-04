package com.example.motusgame.ui.resultScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.motusgame.R
import com.example.motusgame.databinding.FragmentResultBinding
import com.example.motusgame.ui.gameScreen.GameFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ResultFragment : Fragment() {


    private var _binding: FragmentResultBinding? = null
    private val binding
        get() = _binding!!
    private val isSuccessful by lazy { arguments?.getBoolean(IS_RESULT_SUCCESS) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(layoutInflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    private fun initViews() {
        with(binding) {
            if (isSuccessful == true) {
                resultIcon.setImageResource(R.drawable.ic_success)
                resultMessage.text = getString(R.string.success_message)
            } else {
                resultIcon.setImageResource(R.drawable.ic_failure)
                resultMessage.text = getString(R.string.failure_message)
            }
        }

        initClickListeners()


    }


    private fun initClickListeners() {
        binding.playAgainButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.frame_content, GameFragment()
                ).commit()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val IS_RESULT_SUCCESS = "isSuccessful"


        fun createInstance(result: Boolean): ResultFragment {
            val args = Bundle()
            args.putBoolean(IS_RESULT_SUCCESS, result)
            val fragment = ResultFragment()
            fragment.arguments = args
            return fragment
        }
    }
}