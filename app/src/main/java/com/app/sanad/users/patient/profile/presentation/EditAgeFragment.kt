package com.app.sanad.users.patient.profile.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentEditAgeBinding
import com.app.sanad.util.AGE_GROUP
import kotlin.getValue

class EditAgeFragment: BaseFragment() {

    // Holds currently selected age group as an integer (1, 2, or 3)
    private var currentIntAge = 0

    private lateinit var binding: FragmentEditAgeBinding
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditAgeBinding.inflate(inflater, container, false)

        initializeViews()

        setupClickListener()
        observeViewModel()

        return binding.root
    }

    // Loads current user age group and sets the corresponding radio button
    private fun initializeViews() {
        currentIntAge = viewModel.userProfile().ageGroup!!
        setChoosenAge(currentIntAge)
    }

    // Updates UI based on provided age group value
    private fun setChoosenAge(age: Int?) {
        age?.let {
            when (age) {
                1 -> binding.rbYoung.isChecked = true
                2 -> binding.rbMiddleAge.isChecked = true
                3 -> binding.rbOlder.isChecked = true
            }
        }
    }

    // Handles radio button changes, close/cancel actions, and confirm button
    private fun setupClickListener() {

        // Listen for age group selection changes
        binding.groupRoot.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_young -> currentIntAge = 1
                R.id.rb_middle_age -> currentIntAge = 2
                R.id.rb_older -> currentIntAge = 3
            }
        }

        // Close and cancel both navigate back
        binding.close.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.cancel.setOnClickListener {
            findNavController().popBackStack()
        }

        // Check whether age changed, then proceed
        binding.btnConfirm.setOnClickListener {
            if (currentIntAge == viewModel.userProfile().ageGroup!!) {
                // No change; simply return
                findNavController().popBackStack()
            } else {
                // Perform update
                checkInternetConnection()
            }
        }
    }

    // Ensures internet connection before updating the profile remotely
    private fun checkInternetConnection() {
        if (isConnected()) {
            showProgressDialog()
            viewModel.updateUserProfileRemotely(AGE_GROUP, currentIntAge)
        } else {
            showNoInternetSnackBar(binding.root)
        }
    }

    // Observes the result of the age update request
    private fun observeViewModel() {
        viewModel.status.observe(viewLifecycleOwner) { status ->
            status?.let {
                if (it) {
                    // Update succeeded
                    showToast(getString(R.string.age_group_changed_successfully))
                    findNavController().popBackStack()
                } else {
                    // Update failed
                    showToast(getString(R.string.update_failed))
                }

                // Reset and stop loading indicator
                viewModel.restStatus()
                dismissProgressDialog()
            }
        }
    }
}
