package com.app.sanad.users.patient.tools.breathing.presntaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.app.sanad.databinding.ChoosingDurationDialogBinding
import com.app.sanad.users.patient.tools.breathing.data.entity.Duration

/**
 * DialogFragment for selecting the duration of a breathing exercise
 */
class ChoosingDurationDialog : DialogFragment(), MinutesListener {

    // Shared ViewModel for breathing exercise
    private val viewModel: BreathViewModel by activityViewModels()

    // ViewBinding reference
    private lateinit var binding: ChoosingDurationDialogBinding

    // Adapter for displaying available durations
    private lateinit var minutesAdapter: MinutesAdapter

    /**
     * Inflates layout, initializes RecyclerView and click listeners
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ChoosingDurationDialogBinding.inflate(inflater, container, false)

        // Initialize RecyclerView with available durations
        init()

        // Set up back and confirm button listeners
        setUpListeners()

        return binding.root
    }

    /**
     * Adjust dialog width and horizontal margins when dialog starts
     */
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val params = dialog?.window?.attributes
        params?.horizontalMargin = 0.05f
        dialog?.window?.attributes = params
    }

    /**
     * Sets click listeners for back and confirm buttons
     */
    private fun setUpListeners() {
        // Dismiss dialog on back icon click
        binding.icBack.setOnClickListener {
            dismiss()
        }

        // Confirm selection, reset progress and remaining time
        binding.button.setOnClickListener {
            viewModel.cancelCountdown()
            viewModel.resetProgress()
            viewModel.resetRemainingTime()
            dismiss()
        }
    }

    /**
     * Initializes the RecyclerView with list of durations
     */
    private fun init() {
        val list = viewModel.listOfDurations(requireActivity())

        minutesAdapter = MinutesAdapter(list, this)
        binding.recycler.adapter = minutesAdapter

        // Set up 3-column vertical grid
        binding.recycler.layoutManager = GridLayoutManager(
            requireActivity(),
            3,
            GridLayoutManager.VERTICAL,
            false
        )
    }

    /**
     * Callback from MinutesAdapter when a duration is selected
     *
     * @param duration Selected Duration object
     */
    override fun onItemClicked(duration: Duration) {
        // Update ViewModel with selected duration
        viewModel.currentDuration = duration.durationNumber
        binding.durationDesc.text = duration.description
        // Update UI with image of selected duration
        binding.imageView.setImageResource(duration.image)
        // Enable confirm button
        binding.button.isEnabled = true
        // Update duration text in ViewModel
        viewModel.setTextDuration(duration.durationText)
    }
}
