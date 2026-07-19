package com.app.sanad.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize

data class Partner(
    var idPartner: String? = null,
    var emailPartner: String? = null,
    var namePartner: String? = null,
    var imagePartner: String? = null,
): Parcelable

