package com.app.sanad.users.supporter.tools.cofe.presintaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentSupportCofeStep1Binding
import com.app.sanad.databinding.FragmentSupportCofeStep2Binding


class SupportCofeStep1Fragment : BaseFragment() {

private  lateinit var binding: FragmentSupportCofeStep1Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentSupportCofeStep1Binding.inflate(inflater, container, false)
        binding.constraintNext.setOnClickListener {
            findNavController().navigate(R.id.action_supportCofeStep1Fragment_to_supportCofeStep2Fragment)
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