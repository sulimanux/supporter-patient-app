package com.app.sanad.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    var id: String? = null,
    var type: Int? = null,
    var image: String? = null,
    var link: String? = null,
    var arTitle: String? = null,
    var enTitle: String? = null,
    var arDescription: String? = null,
    var enDescription: String? = null,
    var gender: Int? = null,
    var ageGroup: Int? = null,
    var religion: Boolean? = null,
) : Parcelable


