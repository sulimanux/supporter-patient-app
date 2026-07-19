package com.app.sanad.chatting.presintation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.chatting.data.entity.Message
import com.app.sanad.util.getDateAsString


// RecyclerView adapter for chat messages
class MessagesAdapter(
    private val messages: List<Message>,
    private val userId: String          // Current user ID
) : RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {

    // Inflate message item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view_message, parent, false)
        return ViewHolder(view)
    }

    // Bind message based on sender
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]

        if (message.senderId == userId) {
            setUpSenderMessage(holder, message)
        } else {
            setUpPartnerMessage(holder, message)
        }
    }

    // Partner (received) message UI
    private fun setUpPartnerMessage(holder: ViewHolder, message: Message) {
        holder.sender.visibility = View.GONE
        holder.partner.visibility = View.VISIBLE
        holder.messagePartner.text = message.text
        holder.datePartner.text = getDateAsString(message.timeStamp ?: 0L)
    }

    // Sender (sent) message UI
    private fun setUpSenderMessage(holder: ViewHolder, message: Message) {
        holder.sender.visibility = View.VISIBLE
        holder.partner.visibility = View.GONE
        holder.messageSender.text = message.text
        holder.dateSender.text = getDateAsString(message.timeStamp ?: 0L)
    }

    // Total number of messages
    override fun getItemCount(): Int = messages.size

    // ViewHolder for message item
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Message containers
        val partner: ConstraintLayout = itemView.findViewById(R.id.partner)
        val sender: ConstraintLayout = itemView.findViewById(R.id.sender)

        // Message text
        val messageSender: TextView = itemView.findViewById(R.id.messageSender)
        val messagePartner: TextView = itemView.findViewById(R.id.messagePartner)

        // Message time
        val dateSender: TextView = itemView.findViewById(R.id.dateSender)
        val datePartner: TextView = itemView.findViewById(R.id.datePartner)
    }
}

