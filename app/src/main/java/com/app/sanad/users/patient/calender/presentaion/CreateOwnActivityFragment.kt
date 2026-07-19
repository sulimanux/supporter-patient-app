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
import com.app.sanad.R
import com.app.sanad.databinding.DialogCalenderBinding
import com.app.sanad.databinding.FragmentCreateOwnActivityBinding
import com.app.sanad.users.patient.calender.data.entity.TaskEntity

/**
 * Fragment that allows users to create a custom activity for a specific calendar day.
 *
 * Responsibilities:
 *  - Collect user input for a custom activity name.
 *  - Validate input to ensure the activity name is not empty.
 *  - Add the custom activity as a [TaskEntity] to the current day plan.
 *  - Optionally update an existing task if the fragment is opened in "updating" mode.
 *  - Display a confirmation dialog after successfully adding the custom activity.
 *
 * Interaction with ViewModel:
 *  - Uses [CalenderViewModel] to access the currently picked date ([DayEntity]) and
 *    the list of chosen activities.
 *  - Converts chosen activities into task entities and adds the new custom task.
 *
 * Navigation:
 *  - Returns to the previous fragment after creating or updating the task.
 *
 * UI Elements:
 *  - EditText for entering the activity name.
 *  - Create button to validate and save the activity.
 *  - Back button to navigate back without saving.
 */
@AndroidEntryPoint
class CreateOwnActivityFragment : BaseFragment() {

    private lateinit var binding: FragmentCreateOwnActivityBinding
    private val viewModel: CalenderViewModel by viewModels()
    private var activityName: String = ""
    private var flag: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateOwnActivityBinding.inflate(inflater, container, false)
        val args = CreateOwnActivityFragmentArgs.fromBundle(requireArguments())
        flag = args.flag
        setListener()
        return binding.root
    }

    /**
     * Validates the input fields before creating or updating a task.
     * @return true if input is valid, false otherwise.
     */
    private fun validateInputFields(): Boolean {
        activityName = binding.activityNameField.text.toString().trim()
        if (activityName.isEmpty()) {
            binding.activityNameField.error = getString(R.string.activity_name_is_required)
            binding.activityNameField.requestFocus()
            return false
        }
        return true
    }

    /**
     * Sets click listeners for UI elements:
     *  - Create button to validate input and add the task.
     *  - Back button to navigate back.
     */
    private fun setListener() {
        binding.createButton.setOnClickListener {
            if (validateInputFields()) {
                if (flag == "updating") {
                    addNewTask()
                } else {
                    createDayPlay()
                }
            }
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    /**
     * Adds a new custom activity to the current day plan.
     */
    private fun createDayPlay() {
        val dayEntity = viewModel.getDayEntity()
        val activities = viewModel.getChosenActivities()
        val tasks = viewModel.toTaskEntities(activities, dayEntity.day).toMutableList()
        tasks.add(task())
        viewModel.createDayPlan(dayEntity, tasks)
        showDoneDialog()
    }

    /**
     * Updates an existing task in the ViewModel.
     */
    private fun addNewTask() {
        viewModel.updateTask(task())
        findNavController().popBackStack()
    }

    /**
     * Creates a TaskEntity representing the custom activity.
     */
    private fun task() = TaskEntity(
        day = viewModel.getDayEntity().day,
        nameTask = activityName,
        description = "",
        image = R.drawable.ic_plan_day,
        isCompleted = false,
    )
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
            findNavController().popBackStack()
            dialog.dismiss()
        }

        dialog.show()
    }
}
