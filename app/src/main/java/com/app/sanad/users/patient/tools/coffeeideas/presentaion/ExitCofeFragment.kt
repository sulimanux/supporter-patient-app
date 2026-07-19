package com.app.sanad.users.patient.tools.coffeeideas.presentaion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.databinding.FragmentExitCofeBinding

@AndroidEntryPoint

class ExitCofeFragment : Fragment() {

    private lateinit var binding: FragmentExitCofeBinding
    private val viewModel: CofeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentExitCofeBinding.inflate(inflater)

        binding.exit.setOnClickListener {
            viewModel.clearData()
            activity?.finish()

        }

        return  binding.root

    }



}