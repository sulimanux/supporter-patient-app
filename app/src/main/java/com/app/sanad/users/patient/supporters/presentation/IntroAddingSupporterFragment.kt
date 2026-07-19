package com.app.sanad.users.patient.supporters.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.databinding.FragmentInstructionsBinding
import com.app.sanad.databinding.FragmentIntroAddingSupporterBinding
import kotlin.getValue

class IntroAddingSupporterFragment : Fragment() {

    lateinit var binding: FragmentIntroAddingSupporterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroAddingSupporterBinding.inflate(inflater, container, false)
        binding.btAddNew.setOnClickListener{
            findNavController().navigate(R.id.action_introAddingSupporterFragment_to_instructionsFragment)
        }
        binding.icClose.setOnClickListener {
            activity?.finish()
        }
        return  binding.root
    }
    }