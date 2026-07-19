package com.app.sanad.posts.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.auth.data.entity.UserProfile
import com.app.sanad.util.loadImage
import com.app.sanad.util.log

class ChooseSupportersAdapter(
    val context: Context,
    private val supporters: List<UserProfile>

) :
    RecyclerView.Adapter<ChooseSupportersAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val iconCheck: ImageView = itemView.findViewById(R.id.iconCheck)
    }

    private  var supportersList:MutableList<String> = emptyList<String>().toMutableList()
    private var supportersIds: MutableList<String> = emptyList<String>().toMutableList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_choose_supporter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val supporter = supporters[position]
        holder.name.text = supporter.name
        loadImage(context, supporter.imageUser, holder.imageView)

        val selectedBackground = context.getColor(R.color.green)
        val defaultBackground = context.getColor(R.color.black)

        var isSelected = false
        holder.itemView.setOnClickListener {
            isSelected = !isSelected

            if (isSelected) {
                log("selected $position")
                supportersList.add(supporter.email!!)
                supportersIds.add(supporter.id!!)
                holder.name.setTextColor(selectedBackground)
            } else {
                log("no selected $position")
                supportersList.remove(supporter.email!!)
                supportersIds.remove(supporter.id!!)
                holder.name.setTextColor(defaultBackground)
            }
        }
    }

    fun getSelectedSupporters()= supportersList
    fun getSelectedSupportersIds() = supportersIds

    override fun getItemCount(): Int {
        log("getItemCount ${supporters.size}")
        return supporters.size
    }
}