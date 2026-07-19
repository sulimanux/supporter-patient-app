/**
 * Adapter that renders the user's daily mood tracking history,
 * showing pre/post mood states, reasons, and expandable details.
 */
package com.app.sanad.users.patient.moodTracking.presentaion.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.users.patient.moodTracking.data.entity.DayMoodTracking
import com.app.sanad.users.patient.moodTracking.data.entity.EffectingMood
import com.app.sanad.users.patient.moodTracking.data.entity.EmojiMood

class TrackingMoodAdapter(
    private val list: List<DayMoodTracking>,
    private val effectingMoodList: List<EffectingMood>,
    private val emojisStatus: List<EmojiMood>
) : RecyclerView.Adapter<TrackingMoodAdapter.ViewHolder>() {

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val day: TextView = itemView.findViewById(R.id.day)
        val text: TextView = itemView.findViewById(R.id.text)
        val lable: TextView = itemView.findViewById(R.id.textView50)
        val cardText: ConstraintLayout = itemView.findViewById(R.id.cardText)
        val moodBefore: TextView = itemView.findViewById(R.id.moodBefore)
        val moodAfter: TextView = itemView.findViewById(R.id.moodAfter)
        val extend: ImageView = itemView.findViewById(R.id.extend)
        val imageBefore: ImageView = itemView.findViewById(R.id.imageBefore)
        val imageAfter: ImageView = itemView.findViewById(R.id.imageAfter)
        val containerBefore: ConstraintLayout = itemView.findViewById(R.id.containerBefore)
        val container: ConstraintLayout = itemView.findViewById(R.id.container)
        val containerAfter: ConstraintLayout = itemView.findViewById(R.id.containerAfter)
        val recyclerViewEffectingMood: RecyclerView = itemView.findViewById(R.id.recyclerViewEffectingMood)
    }

    /** Inflates item layout representing a tracked mood day */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_tracking_mood, parent, false)
        return ViewHolder(view)
    }

    /** Returns total tracked mood records */
    override fun getItemCount() = list.size

    /** Binds mood-tracking data and initializes UI handlers */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDay = list[position]
        setUpUi(holder, currentDay)
    }

    /** Populates mood values, expand/collapse, and reasons display */
    private fun setUpUi(holder: ViewHolder, trackingMood: DayMoodTracking) {
        var isExpanded = false
        val preMood = emojisStatus[trackingMood.preMoodIndex!!]
        val postMood = emojisStatus[trackingMood.postMoodIndex!!]

        holder.day.text = trackingMood.day.toString()
        holder.moodBefore.text = preMood.name
        holder.imageBefore.setImageResource(preMood.emoji)
        holder.containerBefore.setBackgroundColor(Color.parseColor(preMood.backgroundColor))

        holder.moodAfter.text = postMood.name
        holder.imageAfter.setImageResource(postMood.emoji)
        holder.containerAfter.setBackgroundColor(Color.parseColor(postMood.backgroundColor))

        holder.extend.setOnClickListener {
            isExpanded = !isExpanded
            holder.container.visibility = if (isExpanded) View.VISIBLE else View.GONE
            holder.extend.setImageResource(
                if (isExpanded) R.drawable.baseline_keyboard_arrow_up_24
                else R.drawable.baseline_keyboard_arrow_down_24
            )
        }

        if (!trackingMood.extraReasons.isNullOrEmpty()) {
            holder.lable.visibility = View.VISIBLE
            holder.extend.visibility = View.VISIBLE
            holder.text.text = trackingMood.extraReasons
            holder.cardText.visibility = View.VISIBLE
        }

        setUpRecyclerViewEffectingMood(holder, trackingMood)

        if (trackingMood.reasons == null) {
            holder.lable.visibility = View.GONE
            holder.extend.visibility = View.GONE
        }
    }

    /** Renders reasons that impacted the mood for the selected day */
    private fun setUpRecyclerViewEffectingMood(holder: ViewHolder, trackingMood: DayMoodTracking) {
        val list = mutableListOf<EffectingMood>()
        trackingMood.reasons?.forEach {
            list.add(effectingMoodList[it])
        }

        val effectingAdapter = EffectingMoodAdapter(list, false)
        val layoutManager = GridLayoutManager(holder.recyclerViewEffectingMood.context, 2)

        holder.recyclerViewEffectingMood.layoutManager = layoutManager
        holder.recyclerViewEffectingMood.adapter = effectingAdapter
    }
}
