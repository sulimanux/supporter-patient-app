package com.app.sanad.model

data class CurrentTask2(
    val email:String?= null,
    var dayTask:DayTask? = null,
    var status:StatusDailyProgram2? = null,
)