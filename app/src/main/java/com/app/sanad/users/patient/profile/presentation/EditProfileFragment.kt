package com.app.sanad.users.patient.profile.presentation

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.DialogConfirmUpdateReligionBinding
import com.app.sanad.databinding.FragmentEditProfileBinding
import com.app.sanad.util.RELIGION
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.loadImage

@AndroidEntryPoint
class EditProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var sharedPreferences: SharedPreferencesManager

    private var canCheck = true
    private var imageUri: Uri? = null
    private var isPicked = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        sharedPreferences = viewModel.sharedPreferences

        initializeViews()
        setupClickListener()
        observeViewModel()

        return binding.root
    }


    // Initializes UI based on currently stored profile data
    private fun initializeViews() {

        // Load user's religion value
        if (viewModel.userProfile().religion!!) {
            binding.metadata.yes.isChecked = true
        } else {
            binding.metadata.no.isChecked = true
        }

        // Load profile image
        loadImage(requireActivity(), viewModel.userProfile().imageUser, binding.imageUser)

        // Fill text fields for name, age, and gender
        binding.metadata.textName.text = viewModel.userProfile().name
        binding.metadata.textAge.text = getTextAge(viewModel.userProfile().ageGroup)
        binding.metadata.textGender.text = getTextGender(viewModel.userProfile().gender)
    }


    // Handles all click and selection logic for UI components
    private fun setupClickListener() {

        // Tap profile photo to choose new one
        binding.imageUser.setOnClickListener {
            pickImageFromGallery()
        }

        // Save new photo or pick one if none selected yet
        binding.updateImage.setOnClickListener {
            if (isPicked) {
                checkInternetConnection()
            } else {
                pickImageFromGallery()
            }
        }

        // Listen to religion selection, but only when allowed
        binding.metadata.groupRoot.setOnCheckedChangeListener { _, checkedId ->
            if (canCheck) {
                when (checkedId) {
                    R.id.yes -> showDialog(RELIGION, true)
                    R.id.no -> showDialog(RELIGION, false)
                }
            }
        }

        binding.icBack.setOnClickListener {
            activity?.finish()
        }

        // Navigate to sub-sections for editing fields
        binding.metadata.name.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_changeUserNameFragment)
        }
        binding.metadata.pass.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_changePassFragment)
        }
        binding.metadata.age.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_editAgeFragment)
        }
        binding.metadata.gender.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_editGenderFragment)
        }
    }


    // Ensures internet connection before uploading selected image
    private fun checkInternetConnection() {
        if (isConnected()) {
            showProgressDialog()
            viewModel.uploadImageToFireStorage(imageUri!!)
        } else {
            showNoInternetSnackBar(binding.root)
        }
    }


    // Converts age group integer to readable text
    private fun getTextAge(intAge: Int?): String? {
        var strAge = ""
            intAge?.let {
                when (intAge) {
                    1 -> strAge = getString(R.string.young_adulthood1)
                    2 -> strAge = getString(R.string.middle_age1)
                    3 -> strAge =getString(R.string.older)
                }
            }
       return  strAge
    }

    // Converts gender integer to readable text
    private fun getTextGender(gender: Int?): String? {
        return when (gender) {
            1 -> getString(R.string.male)
            2 -> getString(R.string.female)
            else -> null
        }
    }


    // Shows confirmation dialog before updating religion value
    private fun showDialog(key: String, needReligion: Boolean) {

        sharedDialog = Dialog(requireContext())
        sharedDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dialogBinding = DialogConfirmUpdateReligionBinding.inflate(layoutInflater)
        sharedDialog.setContentView(dialogBinding.root)
        sharedDialog.setCanceledOnTouchOutside(true)

        // Apply transparent background and set width
        sharedDialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val params = attributes
            params.width = (resources.displayMetrics.widthPixels * 0.9).toInt()
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = params
        }

        // Close dialog without changing the selection
        dialogBinding.icClose.setOnClickListener {
            sharedDialog.dismiss()
            resetChecked(needReligion)
        }

        // Confirm update
        dialogBinding.btnOk.setOnClickListener {
            sharedDialog.dismiss()
            updateReligion(key, needReligion)
        }

        // Cancel update and revert the radio selection
        dialogBinding.btnCancel.setOnClickListener {
            sharedDialog.dismiss()
            resetChecked(needReligion)
        }

        sharedDialog.show()
    }

    // Updates religion value if network is available
    private fun updateReligion(key: String, needReligion: Boolean) {
        if (isConnected()) {
            showProgressDialog()
            viewModel.updateReligion(key, needReligion)
        } else {
            resetChecked(needReligion)
            showNoInternetSnackBar(binding.root)
        }
    }

    // Reverts the radio selection when dialog is dismissed without confirmation
    private fun resetChecked(value: Boolean) {
        canCheck = false
        if (value) {
            binding.metadata.no.isChecked = true
        } else {
            binding.metadata.yes.isChecked = true
        }
        canCheck = true
    }


    // Receives chosen image from gallery and updates the preview
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                binding.imageUser.setImageURI(it)
                binding.updateImage.text = getString(R.string.save)
                isPicked = true
            }
        }

    // Launches image picker
    private fun pickImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }


    // Observes update status for all profile edits (image, religion, etc.)
    private fun observeViewModel() {
        viewModel.status.observe(viewLifecycleOwner) { status ->
            status?.let {
                if (it) {
                    showToast(getString(R.string.update_success))
                } else {
                    showToast(getString(R.string.update_failed))
                }

                viewModel.restStatus()
                dismissProgressDialog()
            }
        }
    }
}
