package com.app.sanad.model

data class StatusDailyProgram2(

    var preChecked: Boolean? = false,
    var postChecked: Boolean? = false,
    var currentLevel: Int? = 1,
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



