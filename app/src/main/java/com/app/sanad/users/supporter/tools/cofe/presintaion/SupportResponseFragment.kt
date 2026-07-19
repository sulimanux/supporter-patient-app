package com.app.sanad.users.supporter.tools.cofe.presintaion

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentSupportResponseBinding
import com.app.sanad.databinding.FriendMessageDialogBinding
import com.app.sanad.auth.data.entity.UserProfile
import com.app.sanad.users.supporter.tools.cofe.data.entity.UserIdea
import com.app.sanad.users.patient.tools.coffeeideas.presentaion.CofeViewModel
import com.app.sanad.util.loadImage

/**
 * Fragment to allow supporters to send a support response to a user's coffee idea.
 * Displays suggested phrases, user's idea, and allows editing a personalized response.
 */
class SupportResponseFragment : BaseFragment() {

    // ViewModels
    private val viewModel: SupportCafeViewModel by activityViewModels()
    private val coffeeViewModel: CofeViewModel by activityViewModels()

    // ViewBinding
    private lateinit var binding: FragmentSupportResponseBinding

    // Partner (user being supported) profile
    private lateinit var partnerProfile: UserProfile

    // User's coffee idea being responded to
    private lateinit var userIdea: UserIdea

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupportResponseBinding.inflate(inflater, container, false)

        // Get partner profile and user's idea from shared ViewModel
        partnerProfile = coffeeViewModel.partnerProfile.value!!
        userIdea = coffeeViewModel.userIdea.value!!

        // Load partner image
        loadImage(requireContext(), partnerProfile.imageUser, binding.userImage)

        // Initialize UI
        setUpListener()
        setSuggestedResponse()
        observeData()

        return binding.root
    }

    /** Sets a default suggested response in the edit text */
    private fun setSuggestedResponse() {
        binding.editText.setText(getString(R.string.phrase1))
    }

    /** Observes LiveData from ViewModels to update UI and handle navigation */
    private fun observeData() {

        // Observe when the response has been successfully shared
        coffeeViewModel.sharingState.observe(viewLifecycleOwner) { shared ->
            shared?.let {
                if (it) {
                    // Navigate to ThanksFragment after sending
                    findNavController().navigate(R.id.action_supportResponseFragment_to_thanksFragment)
                }
                dismissProgressDialog()
                coffeeViewModel.resetSharingState()
            }
        }

        // Observe selected suggested text and append it to edit text
        viewModel.selectedText.observe(viewLifecycleOwner) { text ->
            val previousText = binding.editText.text.toString()
            binding.editText.setText("$previousText $text")
        }
    }

    /** Sets up click listeners for UI elements */
    private fun setUpListener() {
        binding.suggestedSympathy.setOnClickListener {
            // Show suggested phrases dialog
            SuggestedPhrasesSympathyFragment().show(childFragmentManager, "")
        }

        binding.friendIdea.setOnClickListener {
            // Show dialog displaying user's idea
            showDialog()
        }

        binding.exit.setOnClickListener {
            activity?.finish()
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.constraintNext.setOnClickListener {
            val responseText = binding.editText.text.toString()
            if (responseText.isNotEmpty()) {
                // Show loading and send response
                showProgressDialog()
                val userResponse = UserIdea(
                    response = responseText,
                    idea = userIdea.idea,
                    cupIdea = userIdea.cupIdea
                )
                coffeeViewModel.sendSupporterResponse(userResponse)
            } else {
                showToast(getString(R.string.please_enter_your_response))
            }
        }
    }

    /** Displays a dialog showing the partner's original idea */
    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = FriendMessageDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(true)

        // Customize dialog window size and background
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val layoutParams = attributes
            layoutParams.width = (resources.displayMetrics.widthPixels * 0.9).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = layoutParams
        }

        // Set dialog content
        dialogBinding.close.setOnClickListener { dialog.dismiss() }
        dialogBinding.text.text = userIdea.idea
        dialogBinding.userName.text = partnerProfile.name
        loadImage(requireContext(), partnerProfile.imageUser, dialogBinding.imageView)

        dialog.show()
    }
}
