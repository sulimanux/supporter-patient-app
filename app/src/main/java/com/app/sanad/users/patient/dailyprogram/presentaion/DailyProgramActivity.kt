package com.app.sanad.users.patient.dailyprogram.presentaion

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseActivity
import com.app.sanad.util.CHALLENGES_ENGAGEMENT
import com.app.sanad.util.CHALLENGE_SELECTED
import com.app.sanad.util.CHALLENGING_COMPLETED
import com.app.sanad.util.CONTENT_COMPLETED
import com.app.sanad.util.CONTENT_ENGAGEMENT
import com.app.sanad.util.DailyProgramEngagement
import com.app.sanad.util.TIME_SPENT
import com.app.sanad.util.IS_COMPLETED
import com.app.sanad.util.STOPPED_AT
import com.app.sanad.util.Temp
import com.app.sanad.util.durationAsString
import com.app.sanad.util.log

@AndroidEntryPoint
class DailyProgramActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_program)
        log("Logging event: origin=app => DailyProgramActivity")
    }

    override fun onStop() {
        super.onStop()

        val dailyProgramEngagement = durationAsLong()
        Temp.dailyProgramEngagement += dailyProgramEngagement

        trackContent()
        trackChallenges()
           // true  = falese
        if (!Temp.completion){
            logEvent(DailyProgramEngagement) {
                param(TIME_SPENT, durationAsString( Temp.dailyProgramEngagement))
            }
            logEvent(STOPPED_AT) {
                param(STOPPED_AT , Temp.stoppedAt!! )
            }
        }

        Temp.completion = false
    }

    private  fun  trackContent(){
        logEvent(CONTENT_ENGAGEMENT) { param(TIME_SPENT, durationAsString(Temp.contentEngagement)) }
        logEvent(CONTENT_COMPLETED) { param(IS_COMPLETED, Temp.isContentCompleted.toString()) }
    }

    private fun trackChallenges(){
        if (Temp.challengeCompleted){
            logEvent(CHALLENGE_SELECTED) { param(CHALLENGE_SELECTED, Temp.challengeSelected) }
            logEvent(CHALLENGES_ENGAGEMENT) { param(TIME_SPENT, Temp.challengeEngagement.toString()) }
        }
        logEvent(CHALLENGING_COMPLETED) { param(IS_COMPLETED,  Temp.challengeCompleted.toString()) }

    }


}
