package com.app.sanad.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Step (
    val step:String,
    val title: String,
    val description: String,
    val toDo: String = "",
    val image: Int,
    val flag: Int = 1
) : Parcelable