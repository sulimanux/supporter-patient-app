package com.app.sanad.users.patient.dailyprogram.data.entity

data class StatusDailyProgram(

    var preChecked: Boolean? = false,
    var postChecked: Boolean? = false,
    var preMoodIndex: Int? = 0,
    var isDayProgramCompleted: Boolean? = false,

    var postMoodIndex: Int? = 0,
    var reasons:List<Int>? = null,
    var extraReasons:String? = null,


    var day: Int? = 1, // 1
    var remaining: Int? = 3, //4
    var completionRate: Int? = 0, // 0

    var currentIndexEducational: Int? = 0,
    var currentIndexBehavioral: Int? = 0,
    var currentIndexSpiritual: Int? = 0,

    var educational: Int? = 0,
    var behavioral: Int? = 0,
    var spiritual: Int? = 0,




)