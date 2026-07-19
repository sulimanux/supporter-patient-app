package com.app.sanad.users.patient.profile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentChangeUserNameBinding
import com.app.sanad.util.NAME
import com.app.sanad.util.isValidInput

class ChangeUserNameFragment : BaseFragment() {

    // View binding reference
    private lateinit var binding: FragmentChangeUserNameBinding

    // Shared ViewModel instance
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChangeUserNameBinding.inflate(inflater, container, false)

        initializeViews()

        setupClickListener()

        observeViewModel()

        return binding.root
    }

    // Sets initial UI values such as current username
    private fun initializeViews() {
        binding.edCurrentName.setText(viewModel.userProfile().name)
    }

    // Handles back navigation, cancel actions, and save request
    private fun setupClickListener() {
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.confirmation.setOnClickListener {
            val newName = binding.edNewName.text.toString().trim()

            // Validate new username before attempting update
            if (isValidInput(newName)) {
                checkInternetConnection(newName)
            } else {
                showToast(getString(R.string.enter_the_new_name))
            }
        }
    }

    // Ensures internet connection before calling remote update
    private fun checkInternetConnection(newName: String) {
        if (isConnected()) {
            showProgressDialog()
            viewModel.updateUserProfileRemotely(NAME, newName)
        } else {
            showNoInternetSnackBar(binding.root)
        }
    }

    // Observes update status from ViewModel
    private fun observeViewModel() {
        viewModel.status.observe(viewLifecycleOwner) { status ->
            status?.let {
                if (it) {
                    // Successful update
                    showToast(getString(R.string.the_username_has_been_changed_successfully))
                    findNavController().popBackStack()
                } else {
                    // Failed update
                    showToast(getString(R.string.update_failed))
                }

                // Reset state and remove loading UI
                viewModel.restStatus()
                dismissProgressDialog()
            }
        }
    }

    // Ensures the progress dialog is dismissed if the fragment is destroyed
    override fun onDestroy() {
        super.onDestroy()
        dismissProgressDialog()
    }
}
