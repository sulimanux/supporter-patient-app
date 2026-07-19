package com.app.sanad.users.patient.dailyprogram.data.entity

import com.app.sanad.users.patient.moodTracking.data.entity.DayMoodTracking

data class CurrentDay(
    val email:String?= null,
    var dayTask: DayTaskEntity? = null,
    var status: StatusDailyProgram? = null,
){

    fun toDayMoodTracking(): DayMoodTracking {
      return DayMoodTracking(
          postMoodIndex = status?.postMoodIndex,
          preMoodIndex = status?.preMoodIndex,
          reasons = status?.reasons,
          extraReasons = status?.extraReasons,
          day = status?.day,
      )

    }

}