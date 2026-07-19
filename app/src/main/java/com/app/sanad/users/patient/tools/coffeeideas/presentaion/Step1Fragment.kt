package com.app.sanad.users.patient.tools.coffeeideas.presentaion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentStep1Binding
import com.app.sanad.util.REQUIRED
import com.app.sanad.util.THOUGHT_LOGGING_ENGAGEMENT
import com.app.sanad.util.THOUGHT_PROVIDED
import com.app.sanad.util.THOUGHT_TYPE_SELECTED
import com.app.sanad.util.TIME_SPENT
import com.app.sanad.util.Temp
import com.app.sanad.util.USER_SKIP_INPUT
import com.app.sanad.util.durationAsString
import com.app.sanad.util.log
import kotlin.getValue

/**
 * Step 1 of the Coffee Ideas flow.
 *
 * The patient provides their initial thought and selects a "cup" representing the thought type.
 * Handles user input validation, navigation, and logs engagement events.
 */
@AndroidEntryPoint
class Step1Fragment : BaseFragment() {

    // View binding instance
    private lateinit var binding: FragmentStep1Binding

    // Shared ViewModel scoped to the activity
    private val viewModel: CofeViewModel by activityViewModels()

    // Currently selected cup view for highlighting
    private var selectedCup: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using view binding
        binding = FragmentStep1Binding.inflate(inflater)

        // Set up UI listeners (cups, navigation buttons)
        setUpListeners()

        // Bind ViewModel to layout for data binding
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Track where the user stopped/interacted
        Temp.stoppedAt = "Step1Fragment"
    }

    /**
     * Sets up click listeners for navigation, cup selection, and exit/back actions
     */
    private fun setUpListeners() {

        // NEXT button: validates input and navigates
        binding.constraintNext.setOnClickListener {
            if (binding.editText.text.toString().isNotEmpty() && viewModel.cupNumber != 0) {
                // Proceed to next step
                log(viewModel.cupNumber.toString() + " rfgt  fsdfdsf ")
                val  action = Step1FragmentDirections.actionStep1FragmentToStep2Fragment()
                findNavController().navigate(action)
            } else {
                // Handle missing input or missing cup selection
                if (binding.editText.text.toString().isEmpty()) {
                    // Log user skipped thought input
                    logEvent(USER_SKIP_INPUT) {
                        param(REQUIRED,"thought")
                    }
                    showToast(getString(R.string.please_enter_your_idea))
                } else {
                    // Log user skipped cup selection
                    logEvent(USER_SKIP_INPUT) {
                        param(REQUIRED,"thought_type")
                    }
                    showToast(getString(R.string.please_select_the_cup))
                }
            }
        }

        // BACK button
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        // Exit the activity
        binding.exit.setOnClickListener {
            activity?.finish()
        }

        // Cup selection buttons: highlight selected cup and update ViewModel
        binding.chooseCup.cup1.setOnClickListener { view -> updateBackground(view, 1) }
        binding.chooseCup.cup2.setOnClickListener { view -> updateBackground(view, 2) }
        binding.chooseCup.cup3.setOnClickListener { view -> updateBackground(view, 3) }
    }

    override fun onResume() {
        super.onResume()
        // Restore previously selected cup if any
        if (viewModel.cupNumber != 0) {
            val cupView = when (viewModel.cupNumber) {
                1 -> binding.chooseCup.cup1
                2 -> binding.chooseCup.cup2
                3 -> binding.chooseCup.cup3
                else -> null
            }
            cupView?.let {
                updateBackground(it, viewModel.cupNumber)
            }
        }
    }

    /**
     * Updates the background of the selected cup and resets the previous selection
     */
    private fun updateBackground(view: View, cupNumber: Int) {
        log(cupNumber.toString() + "  fsdfdsf ") // Debug log
        // Reset previous selection to white background
        selectedCup?.setBackgroundColor(resources.getColor(R.color.white))
        // Highlight new selection
        view.setBackgroundResource(R.drawable.corner_border_blue2)
        selectedCup = view
        viewModel.cupNumber = cupNumber
    }

    override fun onStop() {
        super.onStop()
        // Calculate duration spent on this fragment
        val duration = durationAsString(durationAsLong())

        // Log whether a cup type was selected
        logEvent(THOUGHT_TYPE_SELECTED) {
            param(THOUGHT_TYPE_SELECTED, (viewModel.cupNumber != 0).toString())
        }

        // Log whether a thought was provided
        logEvent(THOUGHT_PROVIDED) {
            param(THOUGHT_PROVIDED, (binding.editText.text.toString().isNotEmpty()).toString())
        }

        // Log engagement duration
        logEvent(THOUGHT_LOGGING_ENGAGEMENT) {
            param(TIME_SPENT, duration)
        }
    }
}
