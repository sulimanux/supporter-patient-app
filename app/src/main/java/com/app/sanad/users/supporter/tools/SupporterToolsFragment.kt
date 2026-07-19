package com.app.sanad.users.supporter.tools

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentSupporterToolsBinding
import com.app.sanad.users.supporter.tools.cofe.presintaion.SupportCofeActivity
import com.app.sanad.users.patient.tools.coffeeideas.presentaion.CofeViewModel
import com.app.sanad.util.log

/**
 * Fragment that displays supporter tools screen.
 * Handles navigation and observes coffee idea updates
 * to show or hide notification badge.
 */
class SupporterToolsFragment : BaseFragment() {

    // ViewBinding instance for accessing layout views
    private lateinit var binding: FragmentSupporterToolsBinding

    // Shared ViewModel between fragments (activity scope)
    private val viewModel: CofeViewModel by activityViewModels()

    /**
     * Called to create and return the view hierarchy.
     * Initializes binding, sets click listeners,
     * starts listening for idea updates, and observes LiveData.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupporterToolsBinding.inflate(inflater, container, false)

        setupClickListener()

        // Start listening for idea changes from backend or repository
        viewModel.listenToIdeaChanges()

        // Observe LiveData changes
        observeViewModel()

        return binding.root
    }

    /**
     * Observes userIdea LiveData from ViewModel.
     * Shows badge if:
     * - There is a non-empty idea
     * - It has NOT been seen by supporter
     *
     * Hides badge otherwise.
     */
    private fun observeViewModel() {
        viewModel.userIdea.observe(viewLifecycleOwner) { ideaData ->
//            if (ideaData != null && ideaData.idea!!.isNotEmpty()) {
//
//                if (ideaData.seenBySupporter == true) {
//                    // Hide badge if already seen
//                    binding.badge.layout.visibility = View.GONE
//                } else {
//                    // Show badge if not seen
//                    binding.badge.layout.visibility = View.VISIBLE
//                }
//
//            } else {
//                // Hide badge if no idea exists
//                binding.badge.layout.visibility = View.GONE
//            }
        }
    }

    /**
     * Sets click listeners for all tool buttons.
     */
    private fun setupClickListener() {

        // Navigate to Strength Building Plan screen
        binding.strengthBuildingPlan.setOnClickListener {
            findNavController().navigate(
                R.id.action_supporterToolsFragment_to_strengthBuildingPlanFragment
            )
        }

        // Open Support Coffee Activity
        binding.cofe.setOnClickListener {
            startActivity(Intent(requireContext(), SupportCofeActivity::class.java))
        }

        // Navigate to Supporter Compass screen
        binding.supporterCompass.setOnClickListener {
            findNavController().navigate(
                R.id.action_supporterToolsFragment_to_supporterCompassFragment
            )
        }

        // Handle back button click (pop fragment from back stack)
        binding.imageView16.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
