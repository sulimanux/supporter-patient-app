package com.app.sanad.posts.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentDisplayGratitudeBinding
import com.app.sanad.util.getGratitudeQuestionsList


class DisplayGratitudeFragment : BaseFragment() {


    private lateinit var binding: FragmentDisplayGratitudeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentDisplayGratitudeBinding.inflate(inflater, container, false)
        val gratitude = DisplayGratitudeFragmentArgs.fromBundle(requireArguments()).gratitude
        val questions = getGratitudeQuestionsList(requireActivity())
        binding.question.text = questions[gratitude.index]
        binding.text.text = gratitude.answer
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root


    }


}



