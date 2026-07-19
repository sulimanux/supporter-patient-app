package com.app.sanad.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Permissions(
    var allowDailyProgramDetails: Boolean = false,
    var allowMoodTrackingDetails: Boolean = false,
    var allowPrivateMessages: Boolean = false,
    var allowViewReports: Boolean = true,
    var typeReports: Int? = 1,
    var timeRangReports: Int? = 1,
    var allowNotifications: Boolean = true,
    var typeNotifications: Int? = 1,
): Parcelable
