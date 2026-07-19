package com.app.sanad.util

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent

object SessionManager {

    private  var startTime = 0L

    fun onAppForeground(context: Context){
        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        startTime = System.currentTimeMillis()
        firebaseAnalytics.logEvent("start_session"){
            param(TIME_SPENT, startTime)
        }
    }

    fun onAppBackground(context: Context){
        log("onAppBackground -> end session")
        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        val endTime = System.currentTimeMillis() - startTime

        firebaseAnalytics.logEvent("end_session"){
            param(TIME_SPENT, System.currentTimeMillis())
        }
        firebaseAnalytics.logEvent(SESSION_DURATION){
            param(TIME_SPENT, durationAsString(endTime))
        }
    }

}