package com.app.sanad.users.patient.tools.gratitude.presentaion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.databinding.ItemGratitudeBinding
import com.app.sanad.users.patient.tools.gratitude.data.entity.Gratitude

/**
 * RecyclerView adapter responsible for displaying gratitude items.
 *
 * @param gratitudeList List of saved gratitude answers
 * @param suggestedQuestion List of predefined gratitude questions
 */
class GratitudeAdapter(
    private val gratitudeList: List<Gratitude>,
    private val suggestedQuestion: List<String>
) : RecyclerView.Adapter<GratitudeAdapter.GratitudeViewHolder>() {

    /**
     * Inflates the item layout and creates a ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GratitudeViewHolder {
        val binding = ItemGratitudeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GratitudeViewHolder(binding)
    }

    /**
     * Binds data to the ViewHolder at the given position
     */
    override fun onBindViewHolder(holder: GratitudeViewHolder, position: Int) {
        val gratitude = gratitudeList[position]
        holder.bind(gratitude)
    }

    /**
     * Returns total number of gratitude items
     */
    override fun getItemCount(): Int = gratitudeList.size

    /**
     * ViewHolder responsible for binding a single gratitude item
     */
    inner class GratitudeViewHolder(
        private val binding: ItemGratitudeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds gratitude data to UI components
         */
        fun bind(gratitude: Gratitude) {
            // Display the question based on its index
            binding.textViewQuestion.text = suggestedQuestion[gratitude.index]

            // Display the user's gratitude answer
            binding.textViewAnswer.text = gratitude.answer
        }
    }
}
