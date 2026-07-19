package com.app.sanad.users.patient.supporters.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.databinding.FragmentInstructionsBinding
import com.app.sanad.users.patient.supporters.data.entity.Instructions
import kotlin.getValue

@AndroidEntryPoint
class InstructionsFragment : Fragment() {

  lateinit var binding: FragmentInstructionsBinding
    private val viewModel: SupporterViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInstructionsBinding.inflate(inflater, container, false)
         setUpRecyclerView(viewModel.getInstructionsList() , viewModel.currentLang())
        setUpListeners()
        return  binding.root
    }
    private  fun  setUpListeners(){
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
         activity?.finish()
        }
        binding.start.setOnClickListener {
            findNavController().navigate(R.id.action_instructionsFragment_to_supportesFragment)
        }
    }
    private fun setUpRecyclerView(instructionsList: List<Instructions>, currentLang: String) {
        val adapter = InstructionsAdapter(instructionsList , currentLang)
        binding.recyclerViewSteps.adapter = adapter

    }
}