package com.app.sanad.users.patient.dailyprogram.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize



@Entity
@Parcelize
data class DayTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var spiritual: List<Task?>? = null,
    var behaviorActivation: List<Task?>? = null,
    val educational: List<Task?>? = null
) : Parcelable

class TaskListConverter {
    @TypeConverter
    fun fromTaskList(value: List<Task?>?): String? {
        return value?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toTaskList(value: String?): List<Task?>? {
        return value?.let {
            val type = object : TypeToken<List<Task>>() {}.type
            Gson().fromJson(it, type)
        }
    }
}
