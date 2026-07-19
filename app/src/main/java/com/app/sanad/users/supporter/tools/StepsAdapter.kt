package com.app.sanad.users.supporter.tools

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.interfaces.ItemStepsClicked
import com.app.sanad.model.Step

/**
 * RecyclerView Adapter to display a list of steps in supporter tools.
 * Each item shows the step number, title, an image, and an arrow to trigger click actions.
 *
 * @param steps List of Step objects to display
 * @param itemStepsClicked Interface callback to handle arrow clicks
 */
class StepsAdapter(
    private val steps: List<Step>,
    private val itemStepsClicked: ItemStepsClicked
) : RecyclerView.Adapter<StepsAdapter.ViewHolder>() {

    /**
     * ViewHolder holds references to the views of each step item.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val step: TextView = itemView.findViewById(R.id.step)           // TextView for step number
        val title: TextView = itemView.findViewById(R.id.title)         // TextView for step title
        val imageView: ImageView = itemView.findViewById(R.id.imageView) // Image representing the step
        val arrow: ImageView = itemView.findViewById(R.id.arrow)        // Arrow icon to handle clicks
    }

    /**
     * Inflates the item layout and returns a ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_supporter_tools_steps, parent, false)
        return ViewHolder(view)
    }

    /**
     * Binds data to the views for each step item.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val step = steps[position]

        // Set step number and title
        holder.step.text = step.step
        holder.title.text = step.title

        // Set step image
        holder.imageView.setImageResource(step.image)

        // Set click listener on the itemView to notify the callback
        holder.itemView.setOnClickListener {
            itemStepsClicked.onItemClicked(position)
        }
    }

    /**
     * Returns the total number of steps.
     */
    override fun getItemCount() = steps.size
}
