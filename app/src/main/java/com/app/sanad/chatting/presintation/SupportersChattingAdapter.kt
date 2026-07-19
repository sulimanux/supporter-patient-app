package com.app.sanad.chatting.presintation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.auth.data.entity.UserProfile
import com.app.sanad.interfaces.ItemMessagesListClicked
import com.app.sanad.util.loadImage

// Adapter for supporters list used to start a chat
class SupportersChattingAdapter(
    private val itemList: List<UserProfile>,            // Supporters data
    private val context: Context,
    private val itemMessagesListClicked: ItemMessagesListClicked
) : RecyclerView.Adapter<SupportersChattingAdapter.ViewHolder>() {

    // Inflate supporter item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_supporter_chatting, parent, false)
        return ViewHolder(view)
    }

    // Number of supporters
    override fun getItemCount(): Int = itemList.size

    // Bind supporter data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        // Bind UI
        holder.name.text = item.name
        loadImage(context, item.imageUser, holder.imageView)

        // Handle item click
        holder.itemView.setOnClickListener {
            itemMessagesListClicked.onItemClicked(
                item.name!!,
                item.id!!,
                item.imageUser!!,
                position
            )
        }
    }

    // ViewHolder for supporter item
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val imageView: ImageView = itemView.findViewById(R.id.image_user)
    }
}
