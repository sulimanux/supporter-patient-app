package com.app.sanad.users.supporter.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.FragmentStepsBinding
import com.app.sanad.model.Step

/**
 * Fragment to display individual steps in a supporter tool.
 * Handles step navigation (next/back), UI updates, and displaying step details.
 */
class StepsFragment : BaseFragment() {

    // Shared ViewModel scoped to activity
    private val viewModel: SupporterToolsViewModel by activityViewModels()

    // ViewBinding instance
    private lateinit var binding: FragmentStepsBinding

    /**
     * Called to create and return the fragment view.
     * Initializes binding, sets up click listeners, and updates UI for the first step.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStepsBinding.inflate(inflater, container, false)

        // Update UI and content for the initial step
        updateUi(viewModel.getCurrentIndex(), viewModel.getCurrentList().size)
        updateUiData(viewModel.getCurrentIndex(), viewModel.getCurrentList())

        // Set click listeners for navigation
        setupClickListener()

        return binding.root
    }

    /**
     * Sets click listeners for navigation buttons.
     */
    private fun setupClickListener() {

        // Handle "Next" button click
        binding.constraintNext.setOnClickListener {
            val currentIndex = viewModel.getCurrentIndex()
            val listSize = viewModel.getCurrentList().size

            if (currentIndex < listSize - 1) {
                // Move to the next step
                viewModel.setCurrentIndex(currentIndex + 1)
                updateUi(viewModel.getCurrentIndex(), listSize)
                updateUiData(viewModel.getCurrentIndex(), viewModel.getCurrentList())
            } else {
                // If last step, exit fragment
                findNavController().popBackStack()
            }
        }

        // Handle "Back" button click
        binding.back.setOnClickListener {
            viewModel.setCurrentIndex(viewModel.getCurrentIndex() - 1)
            updateUi(viewModel.getCurrentIndex(), viewModel.getCurrentList().size)
            updateUiData(viewModel.getCurrentIndex(), viewModel.getCurrentList())
        }

        // Handle top "Back" icon click
        binding.iconBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    /**
     * Updates the navigation UI based on the current step.
     *
     * @param steps Current step index
     * @param size Total number of steps
     */
    private fun updateUi(steps: Int, size: Int) {
        when (steps) {
            0 -> { // First step
                binding.textNext.text = getString(R.string.explore_more)
                binding.next.visibility = View.GONE
                binding.back.visibility = View.GONE
            }

            in 1 until size - 1 -> { // Middle steps
                binding.textNext.text = getString(R.string.next_step)
                binding.next.visibility = View.VISIBLE
                binding.back.visibility = View.VISIBLE
            }

            size - 1 -> { // Last step
                binding.textNext.text = getString(R.string.finish)
                binding.next.visibility = View.VISIBLE
                binding.next.setImageResource(R.drawable.heart)
                binding.back.visibility = View.GONE
            }
        }
    }

    /**
     * Updates the content of the step UI.
     *
     * @param index Index of the current step
     * @param steps List of all steps
     */
    private fun updateUiData(index: Int, steps: List<Step>) {
        val step = steps[index]

        // Set main step data
        binding.stepNumber.text = step.step
        binding.title.text = step.title
        binding.description.text = step.description
        binding.image.setImageResource(step.image)

        // Set To-Do or Advice section
        if (step.toDo.isNotEmpty()) {
            binding.toDo.text = step.toDo

            // Label based on flag: 1 = To-Do, else = Advice
            binding.labelToDo.text = if (step.flag == 1) {
                getString(R.string.to_do)
            } else {
                getString(R.string.advice)
            }

            binding.toDo.visibility = View.VISIBLE
            binding.linearToDo.visibility = View.VISIBLE
        } else {
            binding.toDo.visibility = View.GONE
            binding.linearToDo.visibility = View.GONE
        }

        // Scroll to top of content
        binding.scrollView.scrollTo(0, 0)
    }
}
