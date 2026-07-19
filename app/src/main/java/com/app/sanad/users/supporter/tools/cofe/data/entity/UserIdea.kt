package com.app.sanad.users.supporter.tools.cofe.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserIdea(
    val idea:String? =null,
    val response:String? = null,
    val cupIdea:Int? = null,
    val seenByPatient:Boolean? = false,
    val seenBySupporter:Boolean? = false,
): Parcelable