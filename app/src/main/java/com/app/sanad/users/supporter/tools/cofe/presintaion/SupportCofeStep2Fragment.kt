package com.app.sanad.users.supporter.tools.cofe.presintaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.databinding.FragmentCupsMeaningBinding
import com.app.sanad.databinding.FragmentSupportCofeStep2Binding
import com.app.sanad.users.patient.tools.coffeeideas.presentaion.CofeViewModel
import com.app.sanad.util.log


class SupportCofeStep2Fragment : Fragment() {

    private  lateinit var binding: FragmentSupportCofeStep2Binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentSupportCofeStep2Binding.inflate(inflater, container, false)
        binding.constraintNext.setOnClickListener {
            findNavController().navigate(R.id.action_supportCofeStep2Fragment_to_supportCofeStep3Fragment)
        }
        binding.exit.setOnClickListener {
            activity?.finish()
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root

    }

}