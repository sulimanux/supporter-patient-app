package com.app.sanad.users.patient.tools.gratitude.presentaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.base.BaseBottomSheetDialogFragment
import com.app.sanad.databinding.FragmentSuggestedGratitudeQuestionsBinding
import com.app.sanad.interfaces.OnConfirmButtonClicked
import com.app.sanad.util.getGratitudeQuestionsList

@AndroidEntryPoint
class SuggestedGratitudeQuestionsFragment(
    private val onConfirmButtonClicked: OnConfirmButtonClicked
) : BaseBottomSheetDialogFragment() {

    // Adapter for displaying suggested gratitude questions
    private lateinit var adapter: SuggestedGratitudeQuestionsAdapter

    // Shared ViewModel to persist selected question state
    private val viewModel: GratitudeViewModel by activityViewModels()

    // ViewBinding reference
    private lateinit var binding: FragmentSuggestedGratitudeQuestionsBinding

    /**
     * Inflates layout and initializes RecyclerView and listeners
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSuggestedGratitudeQuestionsBinding.inflate(inflater, container, false)

        // Initialize list with current selected position
        setUpRecyclerView(
            getGratitudeQuestionsList(requireActivity()),
            viewModel.getSelectedPosition()
        )

        // Setup click listeners
        setListeners()

        return binding.root
    }

    /**
     * Handles close and confirm actions
     */
    private fun setListeners() {
        binding.close.setOnClickListener {
            dismiss()
        }

        binding.buttonConfirm.setOnClickListener {
            updateSelectedPosition()
            dismiss()
        }
    }

    /**
     * Updates selected question in ViewModel and notifies caller
     */
    private fun updateSelectedPosition() {
        val selectedPosition = adapter.getSelectedPosition()
        viewModel.setSelectedPosition(selectedPosition)

        onConfirmButtonClicked.onConfirmClicked(
            viewModel.getQuestion(requireActivity(), selectedPosition)
        )
    }

    /**
     * Sets up RecyclerView with suggested questions
     */
    private fun setUpRecyclerView(
        gratitudeQuestionsList: List<String>,
        selectedPosition: Int
    ) {
        adapter = SuggestedGratitudeQuestionsAdapter(
            gratitudeQuestionsList,
            selectedPosition
        )
        binding.recyclerView.adapter = adapter
    }
}
