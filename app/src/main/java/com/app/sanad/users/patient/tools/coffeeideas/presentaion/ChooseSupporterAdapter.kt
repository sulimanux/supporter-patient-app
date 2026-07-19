/**
 * Adapter responsible for displaying a list of supporters
 * and handling user selection.
 *
 * Package: com.app.sanad.users.patient.tools.coffeeideas.presentaion
 */
package com.app.sanad.users.patient.tools.coffeeideas.presentaion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.app.sanad.R
import com.app.sanad.auth.data.entity.UserProfile
import com.app.sanad.util.loadImage

/**
 * RecyclerView Adapter used to show a list of supporters
 * that the user can choose from.
 *
 * @param supporters list of supporter user profiles
 * @param itemListener callback for handling item clicks
 */
class ChooseSupporterAdapter(
    private val supporters: List<UserProfile>,
    private val itemListener: ItemListener
) : RecyclerView.Adapter<ChooseSupporterAdapter.VH>() {

    /**
     * ViewHolder that holds references to the item views
     * for better performance and cleaner binding.
     */
    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Supporter profile image
        val image = itemView.findViewById<ShapeableImageView>(R.id.imageView)

        // Supporter name text
        val textName = itemView.findViewById<TextView>(R.id.name)
    }

    /**
     * Inflates the item layout and creates a ViewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_view_choose_supporter2,
            parent,
            false
        )
        return VH(itemView)
    }

    /**
     * Returns the total number of supporters.
     */
    override fun getItemCount() = supporters.size

    /**
     * Binds supporter data to the ViewHolder at the given position.
     */
    override fun onBindViewHolder(holder: VH, position: Int) {
        val supporter = supporters[position]

        // Set supporter name
        holder.textName.text = supporter.name

        // Clear any previous image to avoid flickering
        holder.image.setImageDrawable(null)

        // Load supporter profile image asynchronously
        loadImage(holder.itemView.context, supporter.imageUser, holder.image)

        // Handle item click and notify listener
        holder.itemView.setOnClickListener {
            itemListener.onItemClick(supporter)
        }
    }
}

/**
 * Listener interface used to handle supporter item clicks.
 */
interface ItemListener {
    fun onItemClick(supporter: UserProfile)
}
