package com.app.sanad.users.supporter.profile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentSupporterProfileBinding
import com.app.sanad.util.loadImage

class SupporterProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentSupporterProfileBinding
   private val viewModel: SupporterProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSupporterProfileBinding.inflate(inflater, container, false)
        initializeViews()
        setupClickListener()
        return binding.root

    }


    private fun initializeViews() {

        binding.nameUser.text = viewModel.user.name
        loadImage(requireActivity(), viewModel.user.imageUser, binding.imageUser)
    }

    private fun setupClickListener() {


    binding.signOut.setOnClickListener{
        showDialogConfirmLogout(viewModel.sharedPreferences)
    }

        binding.settings.setOnClickListener{
             findNavController().navigate(R.id.action_supporterProfileFragment_to_settingsSupporterFragment)
    }

    }
}