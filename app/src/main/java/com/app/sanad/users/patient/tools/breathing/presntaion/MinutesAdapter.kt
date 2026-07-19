package com.app.sanad.users.patient.tools.breathing.presntaion

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.users.patient.tools.breathing.data.entity.Duration
import androidx.core.graphics.toColorInt

/**
 * Adapter for displaying breathing exercise durations
 * Highlights selected duration
 */
class MinutesAdapter(
    private val minutesList: List<Duration>, // list of available durations
    private val listener: MinutesListener     // callback for item clicks
) : RecyclerView.Adapter<MinutesAdapter.ViewHolder>() {

    // Keeps track of the selected position, -1 means none selected
    private var selectedPosition = RecyclerView.NO_POSITION

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val minuteText: TextView = itemView.findViewById(R.id.text)
        val root: ConstraintLayout = itemView.findViewById(R.id.root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_breathing_duration, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Highlight selected item
        if (selectedPosition == position) {
            holder.minuteText.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
            holder.root.backgroundTintList = ColorStateList.valueOf("#204167".toColorInt())
        } else {
            // Normal (unselected) state
            holder.minuteText.setTextColor("#3d5b7c".toColorInt())
            holder.root.backgroundTintList = ColorStateList.valueOf("#ddecf4".toColorInt())
        }

        // Set duration text
        holder.minuteText.text = minutesList[position].durationText

        // Handle click on a duration
        holder.root.setOnClickListener {
            selectedPosition = position        // update selected position
            notifyDataSetChanged()            // refresh all items to update highlight
            listener.onItemClicked(minutesList[position]) // callback to fragment/dialog
        }
    }

    override fun getItemCount(): Int = minutesList.size
}

/**
 * Callback interface to notify when a duration is selected
 */
interface MinutesListener {
    fun onItemClicked(duration: Duration)
}
