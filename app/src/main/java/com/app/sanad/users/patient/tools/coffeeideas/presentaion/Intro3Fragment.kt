package com.app.sanad.users.patient.tools.coffeeideas.presentaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentIntroCofe3Binding
import com.app.sanad.util.FEATURE_STARTED
import com.app.sanad.util.INTRO2
import com.app.sanad.util.Temp
import com.app.sanad.util.durationAsString


class Intro3Fragment : BaseFragment() {


    private lateinit var binding: FragmentIntroCofe3Binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentIntroCofe3Binding.inflate(inflater)

        binding.constraintNext.setOnClickListener {
            logEvent(FEATURE_STARTED){}
            findNavController().navigate(R.id.action_intro3Fragment2_to_step1Fragment)
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
        Temp.stoppedAt = INTRO2

    }

    override fun onStop() {
        super.onStop()
        Temp.introEngagement = durationAsString(durationAsLong())
    }


}