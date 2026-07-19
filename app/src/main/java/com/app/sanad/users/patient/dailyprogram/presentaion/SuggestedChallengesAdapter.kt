/**
 * Adapter for displaying suggested challenge tasks and allowing user selection.
 */
package com.app.sanad.users.patient.dailyprogram.presentaion

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.app.sanad.R
import com.app.sanad.users.patient.dailyprogram.data.entity.Task

class SuggestedChallengesAdapter(
    private val tasks: List<Task>,
    private var selectedPosition: Int,
    private val lang: String
) : RecyclerView.Adapter<SuggestedChallengesAdapter.ViewHolder>() {

    /**
     * Holds UI components for each suggested task item.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val text: TextView = itemView.findViewById(R.id.text)
        val image: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_suggested_challengs, parent, false)
        return ViewHolder(view)
    }

    /**
     * Returns currently selected task index.
     */
    fun getSelectedPosition() = selectedPosition

    /**
     * Binds UI based on task content and selection state.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = tasks[position]
        val desc = if (lang == "en") task.enDescription else task.arDescription

        holder.title.text = if (lang == "en") task.enTitle else task.arTitle
        holder.text.text = Html.fromHtml(desc)
        Glide.with(holder.itemView.context).load(task.image).into(holder.image)

        val context = holder.itemView.context
        val bg = if (selectedPosition == position)
            R.drawable.corner_four_dark_blue
        else
            R.drawable.corner_four_gray

        holder.itemView.background = context.getDrawable(bg)

        holder.itemView.setOnClickListener {
            notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount() = tasks.size
}
