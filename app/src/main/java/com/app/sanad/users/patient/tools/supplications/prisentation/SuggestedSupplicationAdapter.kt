// Package for supplications presentation layer adapters
package com.app.sanad.users.patient.tools.supplications.prisentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.interfaces.ItemSupplicationClicked
import com.app.sanad.model.Supplication

/**
 * Adapter responsible for displaying suggested supplications list
 * and handling click events on each item.
 */
class SuggestedSupplicationAdapter(
    private val items: List<Supplication>,
    private val itemSupplicationClicked: ItemSupplicationClicked
) : RecyclerView.Adapter<SuggestedSupplicationAdapter.ViewHolder>() {

    /**
     * ViewHolder holding views for a single suggested supplication item
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Supplication name text
        val textName: TextView = itemView.findViewById(R.id.textName)

        // Icon used for user interaction (click)
        val icon: ImageView = itemView.findViewById(R.id.icon)
    }

    /**
     * Inflates item layout and creates ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_suggested_supplications, parent, false)
        return ViewHolder(view)
    }

    /**
     * Binds supplication data to the ViewHolder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Set supplication name
        holder.textName.text = item.name

        // Handle icon click and notify listener
        holder.itemView.setOnClickListener {
            itemSupplicationClicked.onItemClicked(it, item)
        }
    }

    /**
     * Returns total number of items
     */
    override fun getItemCount(): Int = items.size
}
