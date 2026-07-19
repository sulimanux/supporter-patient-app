package com.app.sanad.getLibraryContent.presentaion

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.getLibraryContent.data.DepressionMisconception
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.app.sanad.util.localizeNumber

// Adapter for displaying depression misconceptions list
class MisconceptionAdapter(
    private val items: List<DepressionMisconception>,
    private val context: Context,
    private val pageListener: PageListener
) : RecyclerView.Adapter<MisconceptionAdapter.ViewHolder>() {

    // Currently playing item index (for audio / TTS)
    var playingIndex: Int? = null

    // ViewHolder for misconception item
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iconPlay: AppCompatImageView = view.findViewById(R.id.play)
        val title: TextView = view.findViewById(R.id.title)
        val misconceptionText: TextView = view.findViewById(R.id.misconceptionText)
        val truthText: TextView = view.findViewById(R.id.truthText)
        val num: TextView = view.findViewById(R.id.num)
    }

    // Inflate item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_misconceptions_about_depression, parent, false)
        return ViewHolder(view)
    }

    // Bind data and handle play / stop logic
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Set numbered title and text
        holder.num.text = localizeNumber(position + 1, context)
        holder.title.text = item.title
        holder.misconceptionText.text = item.misconception
        holder.truthText.text = item.truth

        // Update play icon state
        holder.iconPlay.setImageResource(
            if (playingIndex == position)
                R.drawable.icon_play_sound
            else
                R.drawable.icon_stop_sound
        )

        // Handle play / stop click
        holder.iconPlay.setOnClickListener {
            if (playingIndex == position) {
                // Stop currently playing item
                playingIndex = null
                pageListener.onStopClicked()
                notifyItemChanged(position)
            } else {
                // Switch playback to new item
                val oldIndex = playingIndex
                playingIndex = position
                val text =
                    "${item.title} ${item.misconception} ${item.truth}"
                pageListener.onPlayIconClicked(position, text)
                if (oldIndex != null) notifyItemChanged(oldIndex)
                notifyItemChanged(position)
            }
        }
    }

    // Total number of items
    override fun getItemCount() = items.size

    // Callback interface for play / stop actions
    interface PageListener {
        fun onPlayIconClicked(position: Int, text: String)
        fun onStopClicked()
    }
}
