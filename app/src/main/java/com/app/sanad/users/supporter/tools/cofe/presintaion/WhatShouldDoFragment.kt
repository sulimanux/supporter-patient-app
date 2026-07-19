package com.app.sanad.users.supporter.tools.cofe.presintaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.databinding.FragmentWhatShouldDoBinding

/**
 * Fragment that explains "What Should Do" in the Support Coffee flow.
 * Provides navigation to the next fragment or exit from the activity.
 */
class WhatShouldDoFragment : Fragment() {

    // ViewBinding instance
    private lateinit var binding: FragmentWhatShouldDoBinding

    /**
     * Called to create and return the fragment view.
     * Sets up click listeners for back, exit, and next navigation.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWhatShouldDoBinding.inflate(inflater, container, false)

        // Navigate back to previous fragment
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        // Exit the activity completely
        binding.exit.setOnClickListener {
            activity?.finish()
        }

        // Navigate to the Support Response fragment
        binding.constraintNext.setOnClickListener {
            findNavController().navigate(R.id.action_whatShouldDoFragment_to_supportResponseFragment)
        }

        return binding.root
    }
}
