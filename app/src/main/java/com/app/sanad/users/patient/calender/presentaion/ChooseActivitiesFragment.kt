package com.app.sanad.users.patient.calender.presentaion

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.DialogCalenderBinding
import com.app.sanad.databinding.FragmentChooseActiviesBinding
import com.app.sanad.users.patient.calender.data.entity.CalenderActivity

/**
 * Fragment that allows users to choose activities for a specific calendar day.
 *
 * Responsibilities:
 *  - Display a list of calendar activities using [CalenderActivitiesAdapter].
 *  - Handle selection of multiple activities and update the UI accordingly.
 *  - Support creating a custom activity if the user selects the last item.
 *  - Interact with [CalenderViewModel] to save chosen activities as [TaskEntity] objects
 *    linked to the selected [DayEntity].
 *  - Show a confirmation dialog once activities are saved.
 *
 * Implements [OnActivityClickListener] to handle user interactions with the RecyclerView items.
 *
 * Key UI elements:
 *  - RecyclerView displaying activities.
 *  - Button to confirm selection of activities.
 *  - Back button to navigate to the previous screen.
 *
 * Navigation:
 *  - Navigates to [CreateOwnActivityFragment] when the user wants to create a custom activity.
 */
@AndroidEntryPoint
class ChooseActivitiesFragment : BaseFragment(), OnActivityClickListener {

    private val viewModel: CalenderViewModel by viewModels()
    private lateinit var adapter: CalenderActivitiesAdapter
    private lateinit var binding: FragmentChooseActiviesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChooseActiviesBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        setListener()
        return binding.root
    }

    /**
     * Sets up click listeners for UI elements including back button and confirm button.
     */
    private fun setListener() {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.button.setOnClickListener {
            val dayEntity = viewModel.getDayEntity()
            val activities = adapter.getChosenActivities().toList()
            val tasks = viewModel.toTaskEntities(activities, dayEntity.day)
            viewModel.createDayPlan(dayEntity, tasks)
            showDoneDialog()
        }
    }

    /**
     * Initializes the RecyclerView with [CalenderActivitiesAdapter] and activity list.
     */
    private fun setUpRecyclerView() {
        adapter = CalenderActivitiesAdapter(
            viewModel.getCalenderActivities(requireActivity()), this
        )
        binding.recyclerView.adapter = adapter
    }


    private fun showDoneDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogCalenderBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(false)

        val window = dialog.window
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val layoutParams = attributes
            layoutParams.width = (resources.displayMetrics.widthPixels * 0.8).toInt()
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            attributes = layoutParams
        }

        dialogBinding.button.setOnClickListener {
            findNavController().popBackStack()
            dialog.dismiss()
        }

        dialog.show()
    }

    /**
     * Callback when the user selects or deselects activities.
     * Updates the confirm button visibility accordingly.
     */
    override fun onAddActivity(activities: Set<CalenderActivity>) {
        binding.button.alpha = if (activities.isNotEmpty()) 1.0f else 0.0f
    }

    /**
     * Callback when the user selects the option to create a custom activity.
     * Navigates to [CreateOwnActivityFragment] and passes the currently chosen activities.
     */
    override fun createCustomActivity() {
        val activities = adapter.getChosenActivities().toList() ?: emptyList()
        viewModel.setChosenActivities(activities)
        val action = ChooseActivitiesFragmentDirections
            .actionChooseActivitiesFragmentToCreateOwnActivityFragment("creating")
        findNavController().navigate(action)
    }
}
