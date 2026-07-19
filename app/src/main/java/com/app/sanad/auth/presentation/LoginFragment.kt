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
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentLoginBinding
import com.app.sanad.users.supporter.main.SupporterScreenActivity
import com.app.sanad.users.patient.main.presentaion.UserScreensActivity
import com.app.sanad.util.SUPPORTER
import com.app.sanad.util.USER_EMAIL

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    // Remember me flag
    private var isRememberMe = false

    // View binding
    private lateinit var binding: FragmentLoginBinding

    // Shared ViewModel with Activity
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        setupClickListener()
        return binding.root
    }

    // Handle UI clicks
    private fun setupClickListener() {
        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            viewModel.clearData()
        }

        binding.forgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_verificationEmailFragment)
        }

        binding.contactUs.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_contactUsFragment)
        }

        binding.logIn.setOnClickListener {
            if (viewModel.validToLogin(requireActivity())) {
                login()
            } else {
                showToast(viewModel.errorMessage!!)
            }
        }
    }

    // Trigger login request
    private fun login() {
        if (isConnected()) {
            showProgressDialog()
            viewModel.login()
        } else {
            showNoInternetSnackBar(binding.root)
        }
    }

    // Save email if Remember Me enabled
    private fun updateRegistrationInfoLocally() {
        if (isRememberMe) {
            viewModel.sharedPreferences.storeString(USER_EMAIL, viewModel.email.value)
        } else {
            viewModel.sharedPreferences.storeString(USER_EMAIL, "")
        }
    }

    // Bind switch state and previously saved email
    private fun setUpSwitch() {
        val email = viewModel.sharedPreferences.getString(USER_EMAIL)
        if (email != "") {
            isRememberMe = true
            binding.switch1.isChecked = true
            viewModel.email.value = email
        }

        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            isRememberMe = isChecked
        }
    }

    override fun onStart() {
        super.onStart()
        setUpSwitch()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        observeViewModel()
    }

    // React to login result
    private fun observeViewModel() {
        viewModel.authStatus.observe(viewLifecycleOwner) {
            it?.let {
                dismissProgressDialog()

                if (it.isNotEmpty()) {
                    showToast(it)
                } else {
                    viewModel.updateAuthStatusLocale(false)
                    updateRegistrationInfoLocally()
                    showToast(getString(R.string.welcome))
                    baseViewModel.updateUserPropertyAnalytics()
                    navigateBasedUserType()
                }

                viewModel.resetAuthStatus()
            }
        }
    }

    // Navigate to correct dashboard based on user type
    private fun navigateBasedUserType() {
        val userType = viewModel.currentUserProfile().typeOfUser

        if (userType == SUPPORTER) {
            startActivity(Intent(requireContext(), SupporterScreenActivity::class.java))
        } else {
            startActivity(Intent(requireContext(), UserScreensActivity::class.java))
        }
    }
}
