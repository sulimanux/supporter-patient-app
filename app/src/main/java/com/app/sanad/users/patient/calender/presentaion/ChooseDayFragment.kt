package com.app.sanad.users.patient.calender.presentaion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseBottomSheetDialogFragment
import com.app.sanad.databinding.FragmentChooseDayBinding

/**
 * Bottom sheet fragment that allows the user to select a day from a calendar view.
 *
 * Responsibilities:
 *  - Display a calendar for the user to pick a specific date.
 *  - Update the [CalenderViewModel] with the selected date.
 *  - Enable navigation to [ChooseActivitiesFragment] after a date is selected.
 *
 * UI Elements:
 *  - CalendarView for date selection.
 *  - Start button to confirm the chosen date and proceed.
 *
 * Navigation:
 *  - Dismisses the bottom sheet and navigates to the activities selection screen.
 */


class ChooseDayFragment : BaseBottomSheetDialogFragment() {

    private lateinit var binding: FragmentChooseDayBinding
    private val viewModel: CalenderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooseDayBinding.inflate(inflater, container, false)
        setUpCalenderView()
        setListeners()
        return binding.root
    }

    /**
     * Sets up the calendar view listener to handle date selection.
     * Updates the start button visibility and sets the picked date in the ViewModel.
     */
    private fun setUpCalenderView() {
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            binding.startButton.alpha = 1.0f
            viewModel.setPickedDate(date)
        }
    }

    /**
     * Sets click listeners for UI elements.
     * -  dismisses the bottom sheet and navigates to ChooseActivitiesFragment.
     */
    private fun setListeners() {
        binding.startButton.setOnClickListener {
            dismiss()
            requireActivity().findNavController(R.id.nav_host_auth)
                .navigate(R.id.action_dailyPlannerFragment_to_chooseActivitiesFragment)
        }
    }
}
