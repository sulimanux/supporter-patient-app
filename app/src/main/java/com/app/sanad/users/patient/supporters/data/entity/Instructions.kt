package com.app.sanad.users.patient.supporters.data.entity

import androidx.annotation.DrawableRes

data class Instructions(
    @DrawableRes val image: Int,
    val titleAr: String,
    val titleEn: String,
    val descAr: String,
    val descEn: String
)
