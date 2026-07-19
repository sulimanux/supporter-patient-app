package com.app.sanad.users.patient.profile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentChangePassBinding
import com.app.sanad.util.isValidInput
import kotlin.getValue

class ChangePassFragment : BaseFragment() {

    // Holds user-input passwords once collected
    private lateinit var currentPassword: String
    private lateinit var newPassword: String
    private lateinit var confirmNewPassword: String

    // Shared ViewModel between fragments
    private val viewModel: ProfileViewModel by activityViewModels()

    // View binding for this fragment
    private lateinit var binding: FragmentChangePassBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePassBinding.inflate(inflater, container, false)

        // Setup all click listeners
        setupClickListener()

        // Start observing ViewModel LiveData
        observeViewModel()

        return binding.root
    }


    // Handles all button and icon clicks inside the layout
    private fun setupClickListener() {
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnConfirm.setOnClickListener {
            // Validate inputs before making network request
            if (areInputsValid()) {
                checkInternetConnection()
            }
        }
    }

    // Ensures device is online before invoking password change request
    private fun checkInternetConnection() {
        if (isConnected()) {
            showProgressDialog()
            viewModel.changeUserPassword(currentPassword, newPassword)
        } else {
            showNoInternetSnackBar(binding.root)
        }
    }

    // Retrieves text from EditTexts and assigns them to variables
    private fun assignPasswordInputs() {
        currentPassword = binding.editCurrentPassword.text.toString()
        newPassword = binding.editNewPassword.text.toString()
        confirmNewPassword = binding.editConfirmNewPass.text.toString()
    }

    // Validates password fields and shows appropriate error messages
    private fun areInputsValid(): Boolean {
        assignPasswordInputs()

        return if (!isValidInput(currentPassword)) {
            showToast(getString(R.string.enter_current_password))
            false

        } else if (!isValidInput(newPassword)) {
            showToast(getString(R.string.enter_new_password))
            false

        } else if (newPassword.trim().length < 6) {
            showToast(getString(R.string.pass_error))
            false

        } else if (!isValidInput(confirmNewPassword)) {
            showToast(getString(R.string.confirm_new_password))
            false

        } else if (confirmNewPassword.trim().length < 6) {
            showToast(getString(R.string.pass_error))
            false

        } else if (confirmNewPassword != newPassword) {
            showToast(getString(R.string.passwords_do_not_match))
            false

        } else {
            true
        }
    }

    // Observes ViewModel result for password change operation
    private fun observeViewModel() {
        viewModel.status.observe(viewLifecycleOwner) { result ->
            result?.let {
                // Successful password update
                if (it) {
                    showToast(getString(R.string.password_changed_successfully))
                    findNavController().popBackStack()

                    // Failed update
                } else {
                    showToast(getString(R.string.update_failed))
                }

                // Reset status in ViewModel
                viewModel.restStatus()

                // Remove loading indicator
                dismissProgressDialog()
            }
        }
    }
}
