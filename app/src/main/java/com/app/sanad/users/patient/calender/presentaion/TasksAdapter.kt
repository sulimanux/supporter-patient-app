package com.app.sanad.users.patient.calender.presentaion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.users.patient.calender.data.entity.TaskEntity

/**
 * RecyclerView adapter for displaying a list of tasks for a specific day.
 *
 * Responsibilities:
 *  - Bind task data ([TaskEntity]) to the UI components in each item view.
 *  - Display task name, image, and completion status visually.
 *  - Handle user interactions such as marking a task complete/incomplete and deleting tasks via swipe.
 *
 * Interaction:
 *  - Uses [OnItemClickListener] to communicate task status updates and deletions to the hosting fragment or ViewModel.
 *
 *
 */
class TasksAdapter(
    private val tasks: MutableList<TaskEntity>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val image: ImageView = itemView.findViewById(R.id.image)
        val imageChecked: ImageView = itemView.findViewById(R.id.image_checked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day_task_calender, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.nameTask
        holder.image.setImageResource(task.image)

        holder.imageChecked.setImageResource(
            if (task.isCompleted) R.drawable.icon_checked22
            else R.drawable.circle_white_border_blue
        )

        holder.itemView.setOnClickListener {
            // false
            task.isCompleted = !task.isCompleted // true

            onItemClickListener.updateTaskStatus(task)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = tasks.size

    /**
     * Removes a task at the given position and notifies the listener for deletion.
     */
    fun removeItem(position: Int) {
        onItemClickListener.deleteTask(tasks[position].taskId)
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getTasks() = tasks
}

/**
 * Listener interface to handle task interactions from the adapter.
 */
interface OnItemClickListener {
    fun updateTaskStatus(task: TaskEntity)
    fun deleteTask(taskId: Int)
}
