package com.app.sanad.users.patient.profile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentEditGenderBinding
import com.app.sanad.util.GENDER
import kotlin.getValue

class EditGenderFragment : BaseFragment() {

    private val viewModel: ProfileViewModel by activityViewModels()
    private var currentIntGender = 0

    private lateinit var binding: FragmentEditGenderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditGenderBinding.inflate(inflater, container, false)

        initializeViews()
        setupClickListener()
        observeViewModel()

        return binding.root
    }

    // Loads the current gender from profile and updates the UI accordingly
    private fun initializeViews() {
        currentIntGender = viewModel.userProfile().gender!!
        changeUserUi(currentIntGender)
    }

    // Ensures network connection before attempting to update the profile
    private fun checkInternetConnection() {
        if (isConnected()) {
            showProgressDialog()
            viewModel.updateUserProfileRemotely(GENDER, currentIntGender)
        } else {
            showNoInternetSnackBar(binding.root)
        }
    }

    // Handles gender selection, confirmation, and close/cancel actions
    private fun setupClickListener() {

        // User selects male
        binding.male.setOnClickListener {
            currentIntGender = 1
            changeUserUi(currentIntGender)
        }

        // User selects female
        binding.female.setOnClickListener {
            currentIntGender = 2
            changeUserUi(currentIntGender)
        }

        // Close button simply returns to previous screen
        binding.close.setOnClickListener {
            findNavController().popBackStack()
        }

        // Cancel button also navigates back
        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }

        // Confirm updates the gender only if changed
        binding.btnConfirm.setOnClickListener {
            if (currentIntGender == viewModel.userProfile().gender!!) {
                // No change; exit
                findNavController().popBackStack()
            } else {
                // Proceed with remote update
                checkInternetConnection()
            }
        }
    }

    // Adjusts UI to reflect selected gender visually
    private fun changeUserUi(type: Int?) {
        when (type) {
            1 -> {
                // Male selected
                binding.male.setBackgroundResource(R.drawable.corner_four_dark_blue)
                binding.maleText.setTextColor(resources.getColor(R.color.white))
                binding.female.setBackgroundResource(R.drawable.corner_four_gray)
                binding.femaleText.setTextColor(resources.getColor(R.color.dark_gray))

                binding.maleStatusCircle.visibility = View.GONE
                binding.maleStatusChecked.visibility = View.VISIBLE
                binding.femaleStatusChecked.visibility = View.GONE
                binding.femaleStatusCircle.visibility = View.VISIBLE
            }

            2 -> {
                // Female selected
                binding.female.setBackgroundResource(R.drawable.corner_four_dark_blue)
                binding.femaleText.setTextColor(resources.getColor(R.color.white))
                binding.male.setBackgroundResource(R.drawable.corner_four_gray)
                binding.maleText.setTextColor(resources.getColor(R.color.dark_gray))

                binding.femaleStatusCircle.visibility = View.GONE
                binding.femaleStatusChecked.visibility = View.VISIBLE
                binding.maleStatusChecked.visibility = View.GONE
                binding.maleStatusCircle.visibility = View.VISIBLE
            }
        }
    }

    // Observes backend update result and handles UI accordingly
    private fun observeViewModel() {
        viewModel.status.observe(viewLifecycleOwner) { status ->
            status?.let {
                if (it) {
                    // Successful gender update
                    showToast(getString(R.string.gender_changed_successfully))
                    findNavController().popBackStack()
                } else {
                    // Update failed
                    showToast(getString(R.string.update_failed))
                }

                viewModel.restStatus()
                dismissProgressDialog()
            }
        }
    }
}
