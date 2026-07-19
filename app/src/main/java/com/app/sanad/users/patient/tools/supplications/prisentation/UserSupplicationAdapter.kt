// Package for supplications presentation layer adapters
package com.app.sanad.users.patient.tools.supplications.prisentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.interfaces.ItemSupplicationClicked
import com.app.sanad.model.Supplication

/**
 * Adapter responsible for displaying user's supplications list
 * and handling item click events.
 */
class UserSupplicationAdapter(

    // List of user-created supplications
    private val items: List<Supplication>,

    // Callback interface for item click events
    private val itemSupplicationClicked: ItemSupplicationClicked

) : RecyclerView.Adapter<UserSupplicationAdapter.ViewHolder>() {

    /**
     * ViewHolder holding views for a single user supplication item
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Supplication name
        val textName: TextView = itemView.findViewById(R.id.textName)

        // Target number of repetitions
        val textNumber: TextView = itemView.findViewById(R.id.textNumber)

        // Icon used to trigger click action
        val icon: ImageView = itemView.findViewById(R.id.icon)
    }

    /**
     * Inflates user supplication item layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_user_supplications, parent, false)
        return ViewHolder(view)
    }

    /**
     * Binds supplication data to the ViewHolder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Set supplication name
        holder.textName.text = item.name

        // Set repetition number
        holder.textNumber.text = item.number.toString()

        // Change icon to horizontal points
        holder.icon.setImageResource(R.drawable.ic_more_horiz)

        // Handle click on icon to show popup menu
        holder.icon.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view)
            popup.menu.add(view.context.getString(R.string.display))
            popup.menu.add(view.context.getString(R.string.edit))
            popup.menu.add(view.context.getString(R.string.delete))

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.title) {
                    view.context.getString(R.string.display) -> {
                        itemSupplicationClicked.onItemClicked(view, item)
                        true
                    }
                    view.context.getString(R.string.edit) -> {
                        itemSupplicationClicked.onEditClicked(item)
                        true
                    }
                    view.context.getString(R.string.delete) -> {
                        itemSupplicationClicked.onDeleteClicked(item)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        // Keep the item view click for "display" as well, or as per user preference
        holder.itemView.setOnClickListener {
            itemSupplicationClicked.onItemClicked(it, item)
        }
    }

    /**
     * Returns total number of items
     */
    override fun getItemCount(): Int = items.size
}
