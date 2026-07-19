package com.app.sanad.users.supporter.tools.supporterCompass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.users.supporter.tools.StepsAdapter
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentSupporterCompassBinding
import com.app.sanad.interfaces.ItemStepsClicked
import com.app.sanad.model.Step
import com.app.sanad.users.supporter.tools.SupporterToolsViewModel
import com.app.sanad.util.data.stepsCompassList

/**
 * Fragment that displays the "Supporter Compass" tool for supporters.
 * Shows a list of steps and navigates to StepsFragment when a step is clicked.
 */
class SupporterCompassFragment : BaseFragment(), ItemStepsClicked {

    // Shared ViewModel scoped to the activity
    private val viewModel: SupporterToolsViewModel by activityViewModels()

    private lateinit var stepsAdapter: StepsAdapter
    private lateinit var binding: FragmentSupporterCompassBinding

    /**
     * Called to create and return the fragment view.
     * Sets up RecyclerView and click listeners.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupporterCompassBinding.inflate(inflater, container, false)

        // Set up top back button
        setupClickListener()

        // Populate RecyclerView with steps
        updateUI(stepsCompassList(requireActivity()))

        return binding.root
    }

    /**
     * Sets click listener for the back icon.
     */
    private fun setupClickListener() {
        binding.iconBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    /**
     * Sets up RecyclerView with StepsAdapter.
     *
     * @param steps List of Step objects
     */
    private fun updateUI(steps: List<Step>) {
        stepsAdapter = StepsAdapter(steps, this)
        binding.recyclerView.adapter = stepsAdapter
    }

    /**
     * Callback when a step is clicked.
     * Stores selected step index and list in ViewModel and navigates to StepsFragment.
     *
     * @param index Index of the clicked step
     */
    override fun onItemClicked(index: Int) {
        viewModel.setCurrentIndex(index)
        viewModel.setCurrentList(stepsCompassList(requireActivity()))

        findNavController().navigate(R.id.action_supporterCompassFragment_to_stepsFragment)
    }
}
