package com.app.sanad.notifications.presentation

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.sanad.R
import com.app.sanad.databinding.ItemNotificationBinding
import com.app.sanad.notifications.data.entities.Notification
import androidx.core.graphics.toColorInt
import com.app.sanad.notifications.data.entities.NotificationsEnum
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.getGratitudeQuestionsList
import com.app.sanad.util.log

class NotificationsAdapter(
    private val sharedPreferences: SharedPreferencesManager,
    private val onDelete: (Notification) -> Unit,
    private val onItemClicked: OnItemClicked
) : ListAdapter<Notification, NotificationsAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(old: Notification, new: Notification) = old.id == new.id
        override fun areContentsTheSame(old: Notification, new: Notification) = old == new
    }

    inner class VH(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        log("onBindViewHolder ${item.read}")

        with(holder.binding) {
            val currentLang = sharedPreferences.getString(com.app.sanad.util.LANGUAGE)
            tvBody.text = if (currentLang == "en") {
                item.bodyEn ?: item.body
            }
            else {
                item.bodyAr ?: item.body
            }

            tvTime.text = ""
//            tvTime.text = holder.itemView.context.getTimeAgo(item.time!!)
            if (!item.read!!){
                root.setBackgroundColor("#ecf8fe".toColorInt())
            }else{
                root.setBackgroundColor("#ffffff".toColorInt())
            }
            setImageBasedType(item.type!!, holder)
        }

        holder.binding.icDelete.setOnClickListener {
            onDelete(item)
        }

        holder.itemView.setOnClickListener {
            onItemClicked.onItemClicked(item)
        }

    }
    fun setImageBasedType(type: String , holder: VH){
        when (type) {
            NotificationsEnum.Sharing.toString() -> {
                holder.binding.imageView.setImageResource(R.drawable.ic_share2)
            }
            NotificationsEnum.Coffee.toString() -> {
                holder.binding.imageView.setImageResource(R.drawable.thought_restcu_back)
            }
            NotificationsEnum.Chat.toString() -> {
                holder.binding.imageView.setImageResource(R.drawable.message_gratitude)
            }
        }

    }

    fun Context.getTimeAgo(time: Long): String {
        log("tiem is $time")
        val diff = System.currentTimeMillis() - time

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            seconds < 60 -> getString(R.string.just_now)

            minutes < 60 ->
                resources.getQuantityString(
                    R.plurals.minutes_ago,
                    minutes.toInt(),
                    minutes
                )

            hours < 24 ->
                resources.getQuantityString(
                    R.plurals.hours_ago,
                    hours.toInt(),
                    hours
                )

            days == 1L -> getString(R.string.yesterday)

            days < 7 ->
                resources.getQuantityString(
                    R.plurals.days_ago,
                    days.toInt(),
                    days
                )

            else -> {
                val weeks = days / 7
                if (weeks < 4) {
                    resources.getQuantityString(
                        R.plurals.weeks_ago,
                        weeks.toInt(),
                        weeks
                    )
                } else {
                    val months = days / 30
                    if (months < 12) {
                        resources.getQuantityString(
                            R.plurals.months_ago,
                            months.toInt(),
                            months
                        )
                    } else {
                        val years = days / 365
                        resources.getQuantityString(
                            R.plurals.years_ago,
                            years.toInt(),
                            years
                        )
                    }
                }
            }
        }
    }    interface  OnItemClicked{
        fun onItemClicked(notification: Notification)
    }
}