/**
 * Adapter for displaying mood influence options in the mood-tracking feature.
 * Provides multi-select behavior to allow users to choose factors impacting mood.
 */
package com.app.sanad.users.patient.moodTracking.presentaion.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.users.patient.moodTracking.data.entity.EffectingMood

class EffectingMoodAdapter(
    private val list: List<EffectingMood>,
    val isClickable: Boolean = true,
) : RecyclerView.Adapter<EffectingMoodAdapter.ViewHolder>() {

    private var chosenReasons = setOf<Int>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val icChecked: ImageView = itemView.findViewById(R.id.icChecked)
    }

    /** Creates and inflates view holder layout for each mood item */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_effecting_mood, parent, false)
        return ViewHolder(view)
    }

    /** Binds data to views and toggles selection state */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.title.text = data.title
        holder.icon.setImageResource(data.icon)

        holder.itemView.setOnClickListener {
            if (!isClickable) return@setOnClickListener

            if (chosenReasons.contains(position)) {
                holder.icChecked.visibility = View.GONE
                chosenReasons = chosenReasons.minus(position)
            } else {
                holder.icChecked.visibility = View.VISIBLE
                chosenReasons = chosenReasons.plus(position)
            }
        }
    }

    /** Returns list of selected mood reason indices */
    fun getChosenReasons() = chosenReasons

    /** Returns number of mood items */
    override fun getItemCount() = list.size
}

interface OnItemListener {
    fun onItemClicked(size: Int)
}
