package com.app.sanad.users.patient.calender.presentaion

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.users.patient.calender.data.entity.CalenderActivity

/**
 * RecyclerView Adapter for displaying a list of [CalenderActivity] items in a calendar view.
 *
 * Responsibilities:
 *  - Bind calendar activity data to RecyclerView items.
 *  - Highlight selected activities with a check mark.
 *  - Handle user interaction for selecting activities or creating a custom activity.
 *
 * @property activities List of [CalenderActivity] objects to display.
 * @property onActivityClickListener Listener interface to handle activity selection and custom activity creation.
 */
class CalenderActivitiesAdapter(
    private val activities: List<CalenderActivity>,
    private val onActivityClickListener: OnActivityClickListener,
) : RecyclerView.Adapter<CalenderActivitiesAdapter.ViewHolder>() {

    // Keeps track of currently selected activities
    private var chosenActivities = setOf<CalenderActivity>()

    /**
     * ViewHolder class holding references to item views for efficient recycling.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val container: ConstraintLayout = itemView.findViewById(R.id.container)
        val text: TextView = itemView.findViewById(R.id.text)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val icChecked: ImageView = itemView.findViewById(R.id.icChecked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_activity_calender, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activities[position]

        // Bind activity data
        holder.text.text = activity.nameTask
        holder.imageView.setImageResource(activity.image)
        holder.container.setBackgroundColor(Color.parseColor(activity.background))

        holder.container.setOnClickListener {
            // If the last item is clicked, trigger custom activity creation
            if (position == activities.size - 1) {
                onActivityClickListener.createCustomActivity()
            } else {
                // Toggle selection
                if (chosenActivities.contains(activity)) {
                    chosenActivities = chosenActivities.minus(activity)
                    holder.icChecked.visibility = View.GONE
                } else {
                    chosenActivities = chosenActivities.plus(activity)
                    holder.icChecked.visibility = View.VISIBLE
                }
                onActivityClickListener.onAddActivity(chosenActivities)
            }
        }
    }

    override fun getItemCount() = activities.size

    /**
     * Returns the set of currently selected activities.
     */
    fun getChosenActivities() = chosenActivities
}

/**
 * Listener interface for handling user interactions with calendar activities.
 */
interface OnActivityClickListener {
    /**
     * Called when the user selects or deselects activities.
     *
     * @param activities Set of currently selected [CalenderActivity] objects.
     */
    fun onAddActivity(activities: Set<CalenderActivity>)

    /**
     * Called when the user wants to create a custom activity (last item clicked).
     */
    fun createCustomActivity()
}


