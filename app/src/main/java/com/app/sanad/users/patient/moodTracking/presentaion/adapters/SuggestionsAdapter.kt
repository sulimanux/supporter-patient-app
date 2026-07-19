/**
 * Adapter responsible for displaying actionable mood-improvement suggestions
 * based on the selected emoji mood state.
 */
package com.app.sanad.users.patient.moodTracking.presentaion.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.users.patient.moodTracking.data.entity.EmojiMood

class SuggestionsAdapter(
    private val emoji: EmojiMood
) : RecyclerView.Adapter<SuggestionsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val index: TextView = itemView.findViewById(R.id.index)
        val todoText: TextView = itemView.findViewById(R.id.todo_text)
        val tip: TextView = itemView.findViewById(R.id.tip)
    }

    /** Inflates suggestion item layout and creates ViewHolder */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_resault_mood, parent, false)
        return ViewHolder(view)
    }

    /** Binds suggestion data and applies color-theming based on mood */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.index.text = "${position + 1}"
        holder.index.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(emoji.buttonColor))

        holder.todoText.text = emoji.suggestion[position].text

        if (emoji.suggestion[position].tip.isNotEmpty()) {
            holder.tip.text = emoji.suggestion[position].tip
            holder.tip.setTextColor(Color.parseColor(emoji.buttonColor))
            holder.tip.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor(emoji.tipBackgroundColor))
            holder.tip.visibility = View.VISIBLE
        }
    }

    /** Returns the total number of suggestions for the selected mood */
    override fun getItemCount() = emoji.suggestion.size
}
