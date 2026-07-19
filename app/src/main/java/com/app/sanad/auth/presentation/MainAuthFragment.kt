package com.app.sanad.auth.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentMainAuthBinding

@AndroidEntryPoint
class MainAuthFragment : BaseFragment() {

    // View binding
    private lateinit var binding: FragmentMainAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate UI
        binding = FragmentMainAuthBinding.inflate(inflater)
        setupClickListener()
        return binding.root
    }

    // Handle main auth buttons
    fun setupClickListener() {
        // Go to signup
        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_mainAuthFragment_to_signUpFragment)
        }

        // Go to login
        binding.logIn.setOnClickListener {
            findNavController().navigate(R.id.action_mainAuthFragment_to_loginFragment)
        }

        // Open language bottom sheet
        binding.chooseLang.setOnClickListener {
            LanguageFragment().show(childFragmentManager, LanguageFragment::class.java.name)
        }
    }
}
