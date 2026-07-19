/**
 * Adapter responsible for displaying mood emojis in the mood-tracking UI.
 * Allows users to select an emoji representing their emotional state.
 */
package com.app.sanad.users.patient.moodTracking.presentaion.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.users.patient.moodTracking.data.entity.EmojiMood

class EmojisAdapter(
    private val emojis: List<EmojiMood>,
    private val onEmojiClickListener: OnEmojiClickListener,
) : RecyclerView.Adapter<EmojisAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.mood)
        val emoji: ImageView = itemView.findViewById(R.id.icon)
    }

    /** Inflates emoji item layout and creates ViewHolder */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_mood, parent, false)
        return ViewHolder(view)
    }

    /** Binds emoji data and handles click events */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val emoji = emojis[position]
        holder.title.text = emoji.name
        holder.emoji.setImageResource(emoji.emoji)

        holder.itemView.setOnClickListener {
            onEmojiClickListener.onEmojiClicked(emoji, position)
        }
    }

    /** Returns number of emoji items */
    override fun getItemCount() = emojis.size
}

/** Listener interface for emoji click events */
interface OnEmojiClickListener {
    fun onEmojiClicked(emoji: EmojiMood, index: Int)
}
