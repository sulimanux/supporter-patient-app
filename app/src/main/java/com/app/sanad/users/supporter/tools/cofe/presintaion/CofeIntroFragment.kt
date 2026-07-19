package com.app.sanad.users.supporter.tools.cofe.presintaion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.databinding.FragmentIntroBinding


class CofeIntroFragment : Fragment() {


    private lateinit var binding: FragmentIntroBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentIntroBinding.inflate(inflater)

        binding.constraintNext.setOnClickListener {
            findNavController().navigate(R.id.action_cofeIntroFragment_to_cofeIntro2Fragment)
        }

        binding.back.setOnClickListener {
            activity?.finish()
        }
        return  binding.root
    }

}