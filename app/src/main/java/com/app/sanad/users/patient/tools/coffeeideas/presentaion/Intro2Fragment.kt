package com.app.sanad.users.patient.tools.coffeeideas.presentaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentIntroCofe2Binding
import com.app.sanad.util.INTRO1
import com.app.sanad.util.Temp
import com.app.sanad.util.durationAsString
import com.app.sanad.util.log


class Intro2Fragment : BaseFragment() {
    private lateinit var binding: FragmentIntroCofe2Binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentIntroCofe2Binding.inflate(inflater)

        binding.constraintNext.setOnClickListener {
            findNavController().navigate(R.id.action_intro2Fragment2_to_intro3Fragment2)
        }
        binding.exit.setOnClickListener {
            activity?.finish()
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        Temp.stoppedAt = INTRO1
    }

    override fun onStop() {
        super.onStop()
        Temp.introEngagement = durationAsString(durationAsLong())
    }
}