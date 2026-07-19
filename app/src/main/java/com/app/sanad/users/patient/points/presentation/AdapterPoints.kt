package com.app.sanad.users.patient.points.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.util.log

/**
 * RecyclerView adapter displaying a 30-day points grid
 *
 * @param numberTracking the current number of tracked/completed days
 */
class AdapterPoints(private val numberTracking: Int) :
    RecyclerView.Adapter<AdapterPoints.ViewHolder>() {

    /**
     * ViewHolder for a single day in the points grid
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root: ConstraintLayout = itemView.findViewById(R.id.root)
        val day: TextView = itemView.findViewById(R.id.day)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    /**
     * Inflates the item layout and creates a ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_point, parent, false)
        return ViewHolder(view)
    }

    /**
     * Binds the day number and image/background depending on completion
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
   // 3
        // Set day text (Day 1, Day 2, etc.)
        holder.day.text = holder.itemView.context.getString(R.string.day, position + 1)

        if (position < numberTracking - 1) {
            // Completed day styling
            holder.imageView.setImageResource(R.drawable.star2)
            holder.root.setBackgroundResource(R.drawable.image_background2w)
        } else {
            // Upcoming/incomplete day styling
            holder.imageView.setImageResource(R.drawable.icon_look2)
            holder.root.setBackgroundResource(R.drawable.image_background4j)
        }
    }

    /**
     * Returns total number of days to display (fixed at 30)
     */
    override fun getItemCount() = 30
}
