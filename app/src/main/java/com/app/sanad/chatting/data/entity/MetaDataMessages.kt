package com.app.sanad.chatting.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize

data class MetaDataMessages(
// care
    val nameSupporter:String? = null,
    val idSupporter:String? = null,
    val imageSupporter:String = "",

    //user
    val namePatient:String? = null,
    val idPatient:String? = null,
    val imagePatient:String = "",
): Parcelable

