package com.app.sanad.users.supporter.tools.strengthBuildingPlan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.users.supporter.tools.StepsAdapter
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentStrengthBuildingPlanBinding
import com.app.sanad.interfaces.ItemStepsClicked
import com.app.sanad.model.Step
import com.app.sanad.users.supporter.tools.SupporterToolsViewModel
import com.app.sanad.util.data.stepsBuildStrengthList

/**
 * Fragment to display the list of steps in the "Strength Building Plan" tool.
 * Implements ItemStepsClicked to respond to clicks on individual steps.
 */
class StrengthBuildingPlanFragment : BaseFragment(),
    ItemStepsClicked {

    // Shared ViewModel scoped to activity
    private val viewModel: SupporterToolsViewModel by activityViewModels()

    private lateinit var stepsAdapter: StepsAdapter
    private lateinit var binding: FragmentStrengthBuildingPlanBinding

    /**
     * Called to create and return the fragment view.
     * Sets up RecyclerView with steps and click listeners.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStrengthBuildingPlanBinding.inflate(inflater, container, false)

        // Initialize RecyclerView with the list of strength-building steps
        updateUI(stepsBuildStrengthList(requireActivity()))

        // Set up top back button
        setupClickListener()

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
     * Updates the RecyclerView with the given steps.
     *
     * @param steps List of Step objects
     */
    private fun updateUI(steps: List<Step>) {
        stepsAdapter = StepsAdapter(steps, this)
        binding.recyclerView.adapter = stepsAdapter
    }

    /**
     * Callback when a step item is clicked.
     * Stores the selected index and list in the ViewModel,
     * then navigates to StepsFragment to display the step details.
     *
     * @param index Index of the clicked step
     */
    override fun onItemClicked(index: Int) {
        viewModel.setCurrentIndex(index)
        viewModel.setCurrentList(stepsBuildStrengthList(requireActivity()))

        // Navigate to StepsFragment to show the selected step
        findNavController().navigate(R.id.action_strengthBuildingPlanFragment_to_stepsFragment)
    }
}
