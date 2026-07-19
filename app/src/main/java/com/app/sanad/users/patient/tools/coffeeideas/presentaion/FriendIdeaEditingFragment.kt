package com.app.sanad.users.patient.tools.coffeeideas.presentaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentFriendIdeaEditingBinding
import com.app.sanad.users.supporter.tools.cofe.data.entity.UserIdea
import com.app.sanad.util.IS_THERE_RESPONSE
import com.app.sanad.util.RESPONSE_ENGAGEMENT
import com.app.sanad.util.RESPONSE_VIEWED
import com.app.sanad.util.TIME_SPENT
import com.app.sanad.util.Temp
import com.app.sanad.util.loadImage
import com.app.sanad.util.log
import kotlin.getValue

/**
 * Fragment that allows the patient user to view and edit a friend's idea response.
 * Observes the idea in real-time and updates the UI accordingly.
 */
@AndroidEntryPoint
class FriendIdeaEditingFragment : BaseFragment() {

    // Tracks if the current idea has been viewed by the patient
    private var viewed = false

    // View binding for this fragment
    private lateinit var binding: FragmentFriendIdeaEditingBinding

    // Shared ViewModel for Coffee Ideas, scoped to the activity
    private val viewModel: CofeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout using ViewBinding
        binding = FragmentFriendIdeaEditingBinding.inflate(inflater)

        // Show a loading dialog until data is retrieved
        showProgressDialog()

        // Retrieve the supporter linked to the current user
        viewModel.retrieveSupporter()

        // Start listening for live updates to the idea
        viewModel.listenToIdeaChanges()

        // Observe LiveData for changes
        observeData()

        // Set up click listeners for UI buttons
        setUpListeners()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Track that the fragment was opened and response viewed state
        Temp.stoppedAt = RESPONSE_VIEWED
    }

    /**
     * Observes the LiveData objects from the ViewModel and updates the UI
     */
    private fun observeData() {
        // Observe idea changes
        viewModel.userIdea.observe(viewLifecycleOwner) {
            log("userIdea is $it")
            isThereResponse(it)   // Update UI based on whether a response exists
            dismissProgressDialog()
        }

        // Observe partner profile to display image
        viewModel.partnerProfile.observe(viewLifecycleOwner) {
            it?.let {
                loadImage(requireContext(), it.imageUser, binding.imageView)
            }
        }
    }

    /**
     * Updates the UI depending on whether the idea has a response from the friend
     */
    private fun isThereResponse(idea: UserIdea) {
        if (idea.response != null && idea.response != "") {
            // Friend has responded
            binding.label.visibility = View.VISIBLE
            binding.constraintNext.visibility = View.VISIBLE
            binding.editText.setText(idea.response)
            binding.editText.isEnabled = true
            viewed = true

            // Mark idea as seen by the patient in backend
            viewModel.updateSeenByUser("seenByPatient")
        } else {
            // No response yet
            viewed = false
            binding.editText.setText(context?.getString(R.string.your_friend_rephrases))
            binding.label.visibility = View.GONE
            binding.constraintNext.visibility = View.GONE
        }
    }

    /**
     * Sets up click listeners for navigation and exit/back actions
     */
    private fun setUpListeners() {
        // Proceed to next step
        binding.constraintNext.setOnClickListener {
            findNavController().navigate(R.id.action_friendIdeaEditingFragment_to_step5Fragment)
        }

        // Exit activity
        binding.exit.setOnClickListener {
            activity?.finish()
        }

        // Navigate back
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onStop() {
        super.onStop()

        // Log analytics events
        logEvent(RESPONSE_VIEWED) {
            param(RESPONSE_VIEWED, "true")
        }

        logEvent(IS_THERE_RESPONSE) {
            param(IS_THERE_RESPONSE, if (viewed) "true" else "false")
        }

        logEvent(RESPONSE_ENGAGEMENT) {
            param(TIME_SPENT, duration())   // Track time spent on the fragment
        }
    }
}
