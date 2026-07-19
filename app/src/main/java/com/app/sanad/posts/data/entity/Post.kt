package com.app.sanad.posts.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.app.sanad.getLibraryContent.data.LibraryContent
import com.app.sanad.model.Supplication
import com.app.sanad.users.patient.tools.gratitude.data.entity.Gratitude

@Parcelize
data class Post(
    var type: String?=null,
    var postIndex: Int?=null,
    var supplication: Supplication?=null,
    val libraryContent: LibraryContent?=null,
    val gratitude: Gratitude?=null,
    val supporters:List<String>? = null,
    val listSupportersId:List<String>? = null,
    var seenList:List<String>? = mutableListOf<String>(),
    val timeStamp: Long? = System.currentTimeMillis(),
): Parcelable