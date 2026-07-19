package com.app.sanad.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Supplication(
    var id: String? = null,
    var name: String? = null,
    var number: Int? = null )
    :Parcelable
