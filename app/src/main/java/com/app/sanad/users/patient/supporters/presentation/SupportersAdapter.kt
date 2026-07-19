package com.app.sanad.users.patient.supporters.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.databinding.ItemViewSupporterBinding
import com.app.sanad.auth.data.entity.UserProfile
import com.app.sanad.util.loadImage

class SupportersAdapter
    ( val context:Context,
    private val clickListener: SupporterListener
   ) :
    ListAdapter<UserProfile, SupportersAdapter.SupportersViewHolder>(ItemDiffUtil()) {

    class SupportersViewHolder private constructor(val binding: ItemViewSupporterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            supporter: UserProfile, clickListener: SupporterListener
            ) {

            binding.supporter = supporter
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SupportersViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemViewSupporterBinding.inflate(layoutInflater, parent, false)
                return SupportersViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupportersViewHolder {
        return SupportersViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SupportersViewHolder, position: Int) {
        
        holder.bind(getItem(position), clickListener)
        val supporter = getItem(position)
        holder.binding.nameSupporter.text = supporter.name
        loadImage(holder.binding.root.context, supporter.imageUser!!, holder.binding.imageUser)

        if (supporter.status == 1){
            holder.binding.status.background =    AppCompatResources. getDrawable(context,R.drawable.corner_light_blue)

            holder.binding.status.text = context.getString(R.string.active)
        }else{
            holder.binding.status.background =    AppCompatResources. getDrawable(context,R.drawable.corner_red)
            holder.binding.status.text = context.getString(R.string.not_active)
        }
       
 
    }
}


class ItemDiffUtil() : DiffUtil.ItemCallback<UserProfile>() {
    override fun areItemsTheSame(old: UserProfile, new: UserProfile): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(old: UserProfile, new: UserProfile): Boolean {
        return old.equals(new)
    }

}

class SupporterListener(private val clickListener: (supporter: UserProfile) -> Unit) {
    fun onClick(supporter: UserProfile) = clickListener(supporter)
}