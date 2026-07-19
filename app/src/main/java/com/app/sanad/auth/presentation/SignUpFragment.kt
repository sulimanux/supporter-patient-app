package com.app.sanad.auth.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.databinding.FragmentSignUpBinding
import com.app.sanad.base.BaseFragment
import com.app.sanad.users.supporter.main.SupporterScreenActivity
import com.app.sanad.users.patient.main.presentaion.UserScreensActivity
import com.app.sanad.util.SUPPORTER
import com.app.sanad.util.USER
import com.app.sanad.util.log

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {

    // View binding
    private lateinit var binding: FragmentSignUpBinding

    // Shared auth ViewModel
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        setupClickListener()
        return binding.root
    }

    // Handle button interactions
    fun setupClickListener() {
        binding.chooseUser.user.setOnClickListener {
            viewModel.typeOfUser.value = USER
        }

        binding.chooseUser.supporter.setOnClickListener {
            viewModel.typeOfUser.value = SUPPORTER
        }

        binding.logIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            viewModel.clearData()
        }

        binding.contactUs.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_contactUsFragment)
        }

        binding.age.setOnClickListener {
            AgeFragment().show(childFragmentManager, AgeFragment::class.java.name)
        }

        binding.gender.setOnClickListener {
            GenderFragment().show(childFragmentManager, GenderFragment::class.java.name)
        }

        binding.btnSign.setOnClickListener {
            if (viewModel.validToRegisterUser(requireActivity())) {
                signUp()
            } else {
                showToast(viewModel.errorMessage!!)
            }
        }
    }

    // Trigger registration request
    private fun signUp() {
        if (isConnected()) {
            showProgressDialog()
            viewModel.signUp(requireActivity())
        } else {
            showNoInternetSnackBar(binding.root)
        }
    }

    // Update UI based on selected user type
    private fun changeUserUi(type: String) {
        if (type == USER) {
            binding.chooseUser.user.setBackgroundResource(R.drawable.corner_four_dark_blue)
            binding.chooseUser.patientText.setTextColor(resources.getColor(R.color.white))
            binding.chooseUser.supporter.setBackgroundResource(R.drawable.corner_four_gray)
            binding.chooseUser.careText.setTextColor(resources.getColor(R.color.dark_gray))
            binding.chooseUser.patientStatusCircle.visibility = View.GONE
            binding.chooseUser.patientStatusChecked.visibility = View.VISIBLE
            binding.chooseUser.careStatusChecked.visibility = View.GONE
            binding.chooseUser.careStatusCircle.visibility = View.VISIBLE
        } else {
            binding.chooseUser.supporter.setBackgroundResource(R.drawable.corner_four_dark_blue)
            binding.chooseUser.careText.setTextColor(resources.getColor(R.color.white))
            binding.chooseUser.user.setBackgroundResource(R.drawable.corner_four_gray)
            binding.chooseUser.patientText.setTextColor(resources.getColor(R.color.dark_gray))
            binding.chooseUser.careStatusCircle.visibility = View.GONE
            binding.chooseUser.careStatusChecked.visibility = View.VISIBLE
            binding.chooseUser.patientStatusChecked.visibility = View.GONE
            binding.chooseUser.patientStatusCircle.visibility = View.VISIBLE
        }
    }

    // Show or hide patient fields based on selection
    private fun hideContentUser(needHide: Boolean) {
        if (needHide) {
            binding.additionalContent.visibility = View.VISIBLE
            binding.invitationCode.visibility = View.GONE
        } else {
            binding.invitationCode.visibility = View.VISIBLE
            binding.additionalContent.visibility = View.INVISIBLE
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        observeViewModel()
    }

    // Observe state updates
    private fun observeViewModel() {
        viewModel.strGender.value = getString(R.string.gender)
        viewModel.strAge.value = getString(R.string.age_group)

        viewModel.authStatus.observe(viewLifecycleOwner) {
            dismissProgressDialog()
            it?.let { result ->
                if (result.isNotEmpty()) {
                    showToast(result)
                } else {
                    viewModel.updateAuthStatusLocale(true)
                    showToast(getString(R.string.welcome))
                    baseViewModel.updateUserPropertyAnalytics()
                    navigateBasedUserType()
                }
            }
        }

        viewModel.typeOfUser.observe(viewLifecycleOwner) {
            it?.let { type ->
                hideContentUser(type == USER)
                changeUserUi(type)
            }
        }
    }

    // Route user to correct homepage
    private fun navigateBasedUserType() {

        val userType = viewModel.currentUserProfile().typeOfUser
         log(" navigateBasedUserType() Sign frag $viewModel.currentUserProfile()")
        if (userType == SUPPORTER) {
            log(" navigateBasedUserType() Sign frag $userType")
            startActivity(Intent(requireContext(), SupporterScreenActivity::class.java))
        } else {
            log(" navigateBasedUserType() Sign frag $userType")
            startActivity(Intent(requireContext(), UserScreensActivity::class.java))
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.clearData()
    }
}
