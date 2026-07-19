package com.app.sanad.users.patient.tools.gratitude.presentaion

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R

class SuggestedGratitudeQuestionsAdapter(
    private val items: List<String>,
    private var selectedPosition: Int
) : RecyclerView.Adapter<SuggestedGratitudeQuestionsAdapter.ViewHolder>() {

    /**
     * ViewHolder representing a single suggested question item
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    /**
     * Inflates item layout and creates ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_suggested_questions, parent, false)
        return ViewHolder(view)
    }

    /**
     * Returns the currently selected item position
     */
    fun getSelectedPosition() = selectedPosition

    /**
     * Binds question text and selection state to the ViewHolder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Update UI based on selection state
        if (selectedPosition == position) {
            holder.textView.setTextColor(Color.parseColor("#438fb3"))
            holder.itemView.setBackgroundDrawable(
                holder.itemView.context.resources.getDrawable(
                    R.drawable.corner_four_dark_blue
                )
            )
        } else {
            holder.textView.setTextColor(Color.BLACK)
            holder.itemView.setBackgroundDrawable(
                holder.itemView.context.resources.getDrawable(
                    R.drawable.corner_four_gray
                )
            )
        }

        // Set question text
        holder.textView.text = item

        // Handle item selection
        holder.itemView.setOnClickListener {
            notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
        }
    }

    /**
     * Returns total number of suggested questions
     */
    override fun getItemCount() = items.size
}
