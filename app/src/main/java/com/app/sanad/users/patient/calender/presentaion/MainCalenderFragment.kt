package com.app.sanad.users.patient.calender.presentaion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.base.BaseFragment
import com.app.sanad.R
import com.app.sanad.databinding.FragmentDailyPlanningBinding

/**
 * Fragment that displays the daily planning screen for the user.
 *
 * Responsibilities:
 *  - Show a calendar with all days that have planned activities.
 *  - Allow users to select a day to view or track planned tasks.
 *  - Handle adding new activities for a selected day via [ChooseDayFragment].
 *  - Observe changes in the calendar days and task list from [CalenderViewModel].
 *  - Decorate calendar dates that have tasks using [TaskDecorator].
 *
 * Key UI Elements:
 *  - CalendarView for displaying days and selecting a date.
 *  - Add button to create a new day plan.
 *  - Tracking container for accessing existing day plans.
 *  - Back button to exit the fragment/activity.
 *
 * Interaction with ViewModel:
 *  - Uses [CalenderViewModel] to fetch and observe the list of days with tasks.
 *  - Updates the currently picked date and controls the visibility of the tracking container.
 *
 * Navigation:
 *  - Navigates to [ChooseActivitiesFragment] or [DayPlanFragment] based on user interaction.
 */
@AndroidEntryPoint
class MainCalenderFragment : BaseFragment(), OnDayClickListener {

    private lateinit var binding: FragmentDailyPlanningBinding
    private val viewModel: CalenderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDailyPlanningBinding.inflate(inflater, container, false)
        binding.calendarView.selectedDate = viewModel.today
        viewModel.getDays()
        setListeners()
        observing()
        return binding.root
    }

    /**
     * Sets click listeners for the add button, tracking button, back button,
     * and calendar date selection.
     */
    private fun setListeners() {

        binding.addButton.setOnClickListener {
            ChooseDayFragment().show(childFragmentManager, ChooseDayFragment::class.java.name)
        }

        binding.tracking.setOnClickListener {
            val day = binding.calendarView.selectedDate
            viewModel.setPickedDate(day)
            findNavController().navigate(R.id.action_dailyPlannerFragment_to_dayPlanFragment)
        }

        binding.back.setOnClickListener {
            activity?.finish()
        }

        binding.calendarView.setOnDateChangedListener { _, date, _ ->
            viewModel.daysList.value?.let {
                isHideTrackingContainer(it, date)
            }
        }
    }

    /**
     * Controls the visibility of the tracking container and "no tasks" message
     * based on whether the selected day has planned tasks.
     */
    private fun isHideTrackingContainer(days: HashSet<CalendarDay>, day: CalendarDay) {
        if (days.contains(day)) {
            viewModel.setPickedDate(day)
            binding.trackingContainer.alpha = 1.0f
            binding.noTasks.visibility = View.GONE
        } else {
            binding.trackingContainer.alpha = 0.0f
            binding.noTasks.visibility = View.VISIBLE
        }
    }

    /**
     * Observes LiveData from the ViewModel to update the calendar and task-related UI.
     */
    private fun observing() {
        viewModel.daysList.observe(viewLifecycleOwner) { days ->
            days?.let {
                decorateViews(days)
                isHideTrackingContainer(days, viewModel.today)
            }
        }

        viewModel.taskList.observe(viewLifecycleOwner) { tasks ->
            // Optionally handle task list updates here
        }
    }

    /**
     * Decorates calendar dates that have tasks using [TaskDecorator].
     */

    private fun decorateViews(days: HashSet<CalendarDay>) {

        binding.calendarView.addDecorator(TaskDecorator(days))

    }

    /**
     * Callback for when a day is clicked in the calendar.
     * Sets the picked date in the ViewModel and navigates to the day's plan.
     */
    override fun onDayClick(day: CalendarDay) {
        viewModel.setPickedDate(day)
        findNavController().navigate(R.id.action_dailyPlannerFragment_to_dayPlanFragment)
    }
}
