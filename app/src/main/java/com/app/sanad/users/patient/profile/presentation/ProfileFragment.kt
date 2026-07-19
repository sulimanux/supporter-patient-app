package com.app.sanad.users.patient.profile.presentation

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.DialogAddSupporterBinding
import com.app.sanad.databinding.FragmentProfileBinding
import com.app.sanad.posts.presentation.PostsActivity
import com.app.sanad.users.patient.supporters.presentation.SupportersActivity
import com.app.sanad.util.loadImage
import com.app.sanad.util.log


@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupClickListener()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Set the user's name and profile image when the fragment starts
        binding.nameUser.text = viewModel.userProfile().name
        loadImage(requireActivity(), viewModel.userProfile().imageUser, binding.imageUser)
    }

    /**
     * Sets up click listeners for all interactive UI elements on the profile screen.
     */
    private fun setupClickListener() {
        // Navigate to the EditProfileActivity
        binding.editProfile.setOnClickListener {
            startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
        }

        // Navigate to the PostsActivity
        binding.sharedContent.setOnClickListener {
            startActivity(Intent(requireActivity(), PostsActivity::class.java))
        }

        // Navigate to the mood tracking fragment, passing the user's ID
        binding.reports.setOnClickListener {
            val userId = viewModel.userProfile().id
            val action = ProfileFragmentDirections.actionProfileFragmentToTrackingMoodFragment(userId!!)
            findNavController().navigate(action)
        }

        // Navigate to the MyPointsFragment, passing the current day
        binding.services.myPoints.setOnClickListener {
            val currentDay = viewModel.userProfile().currentDay
            log("Current day is $currentDay")
            val action = ProfileFragmentDirections.actionProfileFragmentToMyPointsFragment(currentDay!!)
            findNavController().navigate(action)
        }

        // Navigate to the SupportersActivity
        binding.services.supporter.setOnClickListener {

//            showDialogAdding()
//
                    startActivity(Intent(requireActivity(), SupportersActivity::class.java))
//            sharedDialog.dismiss()

        }

        // Navigate to the emergency help screen
        binding.services.emergencyHelp.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_numberHelpingFragment2)
        }

        // Navigate to the settings screen
        binding.settings.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        // Navigate to the gratitude list
        binding.gratitude.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_gratitudeListFragment)
        }

        // Show a confirmation dialog for logging out
        binding.logOut.setOnClickListener {
            showDialogConfirmLogout(viewModel.sharedPreferences)
        }
    }



    private fun showDialogAdding() {
        sharedDialog = Dialog(requireContext())
        sharedDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogAddSupporterBinding.inflate(layoutInflater)
        sharedDialog.setContentView(dialogBinding.root)
        sharedDialog.setCanceledOnTouchOutside(true)
//        if (viewModel.userProfile().supportersNumber!! >= 3) {
//            dialogBinding.icClose.visibility = View.GONE
//        }
        val window = sharedDialog.window
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val layoutParams = attributes
            layoutParams.width = (resources.displayMetrics.widthPixels * 0.9).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = layoutParams
        }
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        sharedDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBinding.btAddNew.setOnClickListener {
            startActivity(Intent(requireActivity(), SupportersActivity::class.java))
            sharedDialog.dismiss()
        }
        dialogBinding.icClose.setOnClickListener {
            sharedDialog.dismiss()
        }

        sharedDialog.show()
    }
}