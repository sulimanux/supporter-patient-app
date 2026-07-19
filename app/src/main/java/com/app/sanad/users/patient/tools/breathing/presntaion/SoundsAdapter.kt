package com.app.sanad.users.patient.tools.breathing.presntaion

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.users.patient.tools.breathing.data.entity.Sound

/**
 * Adapter for displaying available breathing sounds
 */
class SoundsAdapter(
    private val sounds: List<Sound>,             // list of sound objects
    private val context: Context,                // context for resources
    private val onItemSoundClicked: OnItemSoundClicked // callback when a sound is clicked
) : RecyclerView.Adapter<SoundsAdapter.ViewHolder>() {

    /**
     * Inflates item view for each sound
     */
    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sound, parent, false)
        return ViewHolder(view)
    }

    /**
     * Binds sound data to the view holder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sound = sounds[position]  // get the sound at current position

        // Set sound title and image
        holder.title.text = sound.name
        holder.imageView.setImageResource(sound.image)
        if(position == selectedPosition){
            holder.constraintUi.setBackgroundResource(R.drawable.corner_border_blue2)
        }else{
            holder.constraintUi.setBackgroundResource(R.drawable.corner_border_white)
        }
        // Handle click on the item
        holder.itemView.setOnClickListener {
            selectedPosition = holder.bindingAdapterPosition
            notifyDataSetChanged()
            onItemSoundClicked.onItemClicked(sound.sound) // notify listener of selected sound ID
        }
    }

    override fun getItemCount(): Int = sounds.size

    /**
     * ViewHolder for sound item
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
        var constraintUi : ConstraintLayout = itemView.findViewById(R.id.constraintUi)
    }
}

/**
 * Callback interface to notify when a sound is selected
 */
interface OnItemSoundClicked {
    fun onItemClicked(soundId: Int?)
}
