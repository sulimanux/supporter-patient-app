package com.app.sanad.users.patient.tools.breathing.presntaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentChoosingBreathDurationBinding
import com.app.sanad.users.patient.tools.breathing.data.entity.Duration
import kotlin.getValue

@AndroidEntryPoint
class ChoosingBreathDurationFragment : BaseFragment(), MinutesListener {

    // Shared ViewModel for breathing exercise
    private val viewModel: BreathViewModel by activityViewModels()

    // ViewBinding reference
    private lateinit var binding: FragmentChoosingBreathDurationBinding

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
        binding = FragmentChoosingBreathDurationBinding.inflate(inflater, container, false)

        // Initialize RecyclerView with list of durations
        init()

        // Setup back and confirm button listeners
        setUpListeners()

        return binding.root
    }

    /**
     * Sets click listeners for back and confirm buttons
     */
    private fun setUpListeners() {
        // Navigate back on back icon click
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Navigate to ChoosingSoundFragment on confirm button click
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_choosingBreathDurationFragment_to_choosingSoundFragment)
        }
    }

    /**
     * Initializes the RecyclerView with available durations
     */
    private fun init() {
        val list = viewModel.listOfDurations(requireActivity())

        minutesAdapter = MinutesAdapter(list,this )
        binding.recycler.adapter = minutesAdapter

        // 3-column vertical grid layout
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
        // Update UI with image of selected duration
        binding.imageView.setImageResource(duration.image)
        binding.durationDesc.text = duration.description
        // Enable confirm button
        binding.button.isEnabled = true

        // Update ViewModel with selected duration
        viewModel.currentDuration = duration.durationNumber   // e.g., 10
        viewModel.setTextDuration(duration.durationText)     // e.g., "10 min"
    }
}
