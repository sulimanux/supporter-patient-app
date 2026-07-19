package com.app.sanad.users.patient.tools.coffeeideas.presentaion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.databinding.FragmentIntroBinding
import com.app.sanad.util.Temp
import kotlin.getValue

@AndroidEntryPoint

class IntroFragment : Fragment() {

    private lateinit var binding: FragmentIntroBinding

    private val viewModel: CofeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentIntroBinding.inflate(inflater)

        binding.constraintNext.setOnClickListener {
            if (viewModel.sharedPreferences.getBoolean("isThereConnection", false)){

                val action = IntroFragmentDirections.actionIntroFragmentToFriendIdeaEditingFragment()
                findNavController().navigate(action)
            }else{
                findNavController().navigate(R.id.action_introFragment_to_introCofe2Fragment2)

            }
        }

        binding.back.setOnClickListener {
            activity?.finish()
        }
        return  binding.root
    }

    override fun onStart() {
        super.onStart()
        Temp.stoppedAt = "IntroFragment"
    }

}