package com.app.sanad.getLibraryContent.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "library_content")

@Parcelize
data class LibraryContent(
    @PrimaryKey val uid: Int? = null,
    val id: Int? = null,
    val date: String? = null,
    val category: String? = null,
    var backgroundColor: String? = null,
    val enText: String? = null,
    val arDescription: String? = null,
    val enDescription: String? = null,
    val arTitle: String? = null,
    val enTitle: String? = null,
    val subCategory: String? = null,
    val type: String? = null,
    val rate: Int? = null,
    val duration: String? = null,
    val viewCount: Int? = null,
    val imageURL: String? = null,
    val videoURL: String? = null,
    val audioURL: String? = null,
    var religion: Boolean? = null,
    ) : Parcelable