package com.app.sanad.users.patient.moodTracking.data.entity

/**
 * Purpose: Represents a single mood-tracking entry for a specific day.
 * Stores mood before/after, reasons, notes, and timestamp for emotional progress tracking.
 */
data class DayMoodTracking (
    val preMoodIndex: Int? = 0,
    val postMoodIndex: Int? = 0,
    val extraReasons: String? = null,
    val reasons: List<Int>? = null,
    val day: Int? = null,
    val date:Long?  = System.currentTimeMillis(),
    ){
    constructor():this(null, null, null, null, null, null)
}
