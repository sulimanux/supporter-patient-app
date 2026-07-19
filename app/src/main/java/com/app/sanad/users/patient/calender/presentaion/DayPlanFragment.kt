package com.app.sanad.users.patient.calender.presentaion

import android.app.Dialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseFragment
import com.app.sanad.databinding.DialogCalenderDonBinding
import com.app.sanad.databinding.FragmentDayPlanBinding
import com.app.sanad.users.patient.calender.data.entity.TaskEntity
import com.app.sanad.util.log

/**
 * Fragment that displays the tasks and daily plan for a selected day.
 *
 * Responsibilities:
 *  - Display a list of tasks for the picked day using [TasksAdapter].
 *  - Track task completion progress and update the UI accordingly.
 *  - Allow the user to add new activities via navigation to [ChooseActivitiesFragment].
 *  - Support task status updates (complete/incomplete) and task deletion.
 *  - Show a "Well Done" dialog when all tasks for the day are completed.
 *  - Enable swipe-to-delete functionality for tasks in the RecyclerView.
 *
 * Interaction with ViewModel:
 *  - Uses [CalenderViewModel] to fetch tasks for the selected [DayEntity].
 *  - Updates tasks and task completion status via ViewModel methods.
 *  - Clears task data when the fragment is stopped to prevent stale data.
 *
 * UI Elements:
 *  - RecyclerView displaying the list of tasks.
 *  - Add button for adding new activities.
 *  - Progress indicator showing task completion.
 *  - "Well Done" dialog for fully completed plans.
 *  - Swipe-to-delete icon for individual tasks.
 *
 * Implements:
 *  - [OnItemClickListener] to handle task updates and deletions from the RecyclerView.
 */
@AndroidEntryPoint
class DayPlanFragment : BaseFragment(), OnItemClickListener {

    private lateinit var binding: FragmentDayPlanBinding
    private lateinit var adapter: TasksAdapter
    private val viewModel: CalenderViewModel by viewModels()
    private var done = 0
    private var taskSize = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDayPlanBinding.inflate(inflater, container, false)

        getTasks(viewModel.getDayEntity().day)
        done = 0
        observing()
        isItToday()
        setUpListeners()
        return binding.root
    }

    /**
     * Checks if the picked date is before, after, or equal to today
     * and adjusts UI elements (like add button visibility) accordingly.
     */
    private fun isItToday() {
        if (viewModel.getPickedDate().isBefore(viewModel.today)) {
            binding.addButton.visibility = View.GONE
            log("yesterday")
        } else if (viewModel.getPickedDate().isAfter(viewModel.today)) {
            log("tomorrow")
        } else {
            log("today")
        }
    }

    /**
     * Sets click listeners for UI elements: add button, back button, and completion button.
     */
    private fun setUpListeners() {
        binding.textButton.setOnClickListener {
            showDoneDialog()
        }

        binding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_dayPlanFragment_to_chooseActivitiesFragment)
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getTasks(day: String) {
        viewModel.getTasks(day)
    }

    private fun observing() {
        viewModel.taskList.observe(viewLifecycleOwner) { tasks ->
            if (tasks.isNotEmpty()) {
                initViews(tasks)
                setUpRecyclerView(tasks)
            }
        }
    }

    private fun initViews(tasks: List<TaskEntity>) {
        taskSize = tasks.size
        binding.numTasks.text = taskSize.toString()
        done = tasks.count { it.isCompleted }
        updateUi(done)
    }

    private fun updateUi(done: Int) {

        binding.numDone.text = done.toString()
        val progress = (done.toFloat() / taskSize.toFloat()) * 100
        binding.circularProgress.progress = progress.toInt()
        if (progress.toInt() == 100) {
            binding.textButton.isEnabled = true
            binding.iconCompleted.visibility = View.VISIBLE
        }else{
            binding.textButton.isEnabled = false
            binding.iconCompleted.visibility = View.GONE
        }
    }

    private fun setUpRecyclerView(tasks: List<TaskEntity>) {
        adapter = TasksAdapter(tasks.toMutableList(), this)
        binding.recyclerView.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }


    override fun updateTaskStatus(task: TaskEntity) {
        // done = 2
        // true
        // done =  3
        if (task.isCompleted) done++ else done--
        updateUi(done)
        viewModel.updateTask(task)
    }

    override fun deleteTask(taskId: Int) {
        viewModel.deleteTask(taskId)
        done = 0
        taskSize = 0
        getTasks(viewModel.getDayEntity().day)
    }

    /**
     * Displays a "Well Done" dialog when all tasks are completed.
     */
    private fun showDoneDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding = DialogCalenderDonBinding.inflate(layoutInflater)
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

        dialogBinding.title.text = getString(R.string.well_done1)
        dialogBinding.text.text = getString(R.string.you_have_completed_your_entire_plan_for_the_day_great_job)
        dialogBinding.button.setOnClickListener {
            findNavController().popBackStack()
            dialog.dismiss()
        }

        dialog.show()
    }

    /**
     * Swipe-to-delete callback for RecyclerView tasks.
     * Draws a delete icon while swiping left.
     */
    private val swipeToDeleteCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            adapter.removeItem(position)
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val itemView = viewHolder.itemView
            if (dX < 0) {
                val deleteIcon = ContextCompat.getDrawable(recyclerView.context, R.drawable.baseline_delete_24)!!
                val intrinsicWidth = deleteIcon.intrinsicWidth
                val intrinsicHeight = deleteIcon.intrinsicHeight
                val iconMargin = (itemView.height - intrinsicHeight) / 2
                val iconLeft = itemView.right - iconMargin - intrinsicWidth
                val iconRight = itemView.right - iconMargin
                val iconTop = itemView.top + iconMargin
                val iconBottom = iconTop + intrinsicHeight
                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                deleteIcon.draw(c)
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.clearData()
    }
}
