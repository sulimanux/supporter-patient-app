package com.app.sanad.chatting.presintation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.app.sanad.R
import com.app.sanad.interfaces.ItemMessagesListClicked
import com.app.sanad.chatting.data.entity.Chatting
import com.app.sanad.chatting.data.entity.Message
import com.app.sanad.util.SUPPORTER
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.loadImage
import com.app.sanad.util.log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// Adapter for chats list (last message per conversation)
class MessagesListAdapter(
    private val messages: List<Chatting>,                    // Conversations
    private val context: Context,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val itemMessagesListClicked: ItemMessagesListClicked
) : RecyclerView.Adapter<MessagesListAdapter.ViewHolder>() {

    var userType = sharedPreferencesManager.getUserProfile().typeOfUser

    // Inflate chat item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view_messages, parent, false)
        return ViewHolder(view)
    }

    // Bind conversation item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = messages[position]

        // Last message in conversation
        val lastMessage = chat.messages?.last()

        // Chat metadata
        val metaData = chat.meta!!

        handleStatusOfSeen( isSeenByUser(lastMessage ) , holder.badge)
        // Partner info
        val (idPartner, imagePartner, namePartner) =
            if (userType == SUPPORTER) {
                Triple(
                    metaData.idPatient!!,
                    metaData.imagePatient,
                    metaData.namePatient!!
                )
            } else {
                Triple(
                    metaData.idSupporter!!,
                    metaData.imageSupporter,
                    metaData.nameSupporter!!
                )
            }

        // Bind UI
        holder.name.text = namePartner
        holder.date.text = formatTimestamp(lastMessage?.timeStamp)
        holder.message.text = lastMessage?.text
        loadImage(context, imagePartner, holder.imageUser)

        // Item click
        holder.itemView.setOnClickListener {
            itemMessagesListClicked.onItemClicked(namePartner, idPartner, imagePartner , position)
        }
    }

    private fun handleStatusOfSeen(bool: Boolean, badge1: View, ) {
        if (bool){
            log("Seen")
            badge1.visibility = View.GONE
        }else{
            log(" not Seen")

            badge1.visibility = View.VISIBLE
        }



    }

    private fun isSeenByUser(
        lastMessage: Message?,
        ): Boolean {

     return if (userType == SUPPORTER)
                lastMessage?.seenBySupporter!!
            else
                lastMessage?.seenByPatient!!
    }

    // Format message time for display
    private fun formatTimestamp(timeStamp: Long?): String {
        if (timeStamp == null) return "Invalid time"

        val currentTime = System.currentTimeMillis()
        val oneDayInMillis = 24 * 60 * 60 * 1000
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

        return when {
            currentTime - timeStamp < 60_000 -> "الان"        // Now
            currentTime - timeStamp < oneDayInMillis -> "اليوم" // Today
            currentTime - timeStamp < 2 * oneDayInMillis -> "الامس" // Yesterday
            else -> dateFormat.format(Date(timeStamp))
        }
    }

    // List size
    override fun getItemCount(): Int = messages.size

    // ViewHolder for chat item
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.name)
        val date: TextView = itemView.findViewById(R.id.date)
        val message: TextView = itemView.findViewById(R.id.message)
        val badge: View = itemView.findViewById(R.id.badge)
        val imageUser: ShapeableImageView =
            itemView.findViewById(R.id.image_user)
    }
}

