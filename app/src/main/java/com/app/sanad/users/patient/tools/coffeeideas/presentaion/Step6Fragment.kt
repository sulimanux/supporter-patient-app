// Package declaration: Defines the namespace of this fragment within the project
package com.app.sanad.users.patient.tools.coffeeideas.presentaion

// Imports: Required classes and functions from Android and project modules
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentStep6Binding
import com.app.sanad.users.patient.calender.presentaion.CalenderActivity
import com.app.sanad.util.ACTIVITY_SCHEDULED
import com.app.sanad.util.Temp

// Annotation for Hilt dependency injection
@AndroidEntryPoint
class Step6Fragment : BaseFragment() {

    // View binding for the fragment's layout
    private lateinit var binding: FragmentStep6Binding

    // Shared ViewModel instance scoped to the activity
    private val viewModel: CofeViewModel by activityViewModels()

    // Boolean to track if the calendar activity was started
    private var activityScheduled = false

    // Called to create the fragment's view hierarchy
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout using view binding
        binding = FragmentStep6Binding.inflate(inflater)

        // Button click listener to navigate to the daily planner (calendar)
        binding.goToDailyPlanner.setOnClickListener {
            // Clear data in ViewModel before leaving
            viewModel.clearData()

            // Mark that the activity is being scheduled
            activityScheduled = true

            // Start the CalendarActivity
            startActivity(Intent(requireContext(), CalenderActivity::class.java))

            // Finish the current activity so user cannot go back
            requireActivity().finish()
        }

        // Button click listener to navigate to the exit fragment
        binding.constraintNext.setOnClickListener {
            findNavController().navigate(R.id.action_step6Fragment_to_exitCofeFragment)
        }

        // Button click listener to exit the current activity
        binding.exit.setOnClickListener {
            activity?.finish()
        }

        // Button click listener to go back to the previous fragment
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        // Return the root view of the binding
        return binding.root
    }

    // Called when the fragment becomes visible
    override fun onStart() {
        super.onStart()
        // Store the fragment name in a temporary object to track where the user stopped
        Temp.stoppedAt = "Step6Fragment"
    }

    // Called when the fragment is no longer visible
    override fun onStop() {
        super.onStop()
        // Log an event to track if the activity was scheduled
        logEvent(ACTIVITY_SCHEDULED) {
            param(ACTIVITY_SCHEDULED, activityScheduled.toString())
        }
    }
}
