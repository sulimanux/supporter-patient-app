package com.app.sanad.users.patient.moodTracking.presentaion.activties

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.base.BaseActivity
import com.app.sanad.databinding.ActivityPreAssessmentBinding
import com.app.sanad.util.DailyProgramEngagement
import com.app.sanad.util.TIME_SPENT
import com.app.sanad.util.IS_COMPLETED
import com.app.sanad.util.PRE_MOOD_TRACKING_COMPLETION
import com.app.sanad.util.PRE_MOOD_TRACKING_DURATION
import com.app.sanad.util.STOPPED_AT
import com.app.sanad.util.Temp
import com.app.sanad.util.durationAsString

@AndroidEntryPoint

class PreAssessmentActivity : BaseActivity() {


    private lateinit var binding: ActivityPreAssessmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreAssessmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStop() {
        super.onStop()
        val preAssessmentDuration = durationAsLong()

        logEvent(PRE_MOOD_TRACKING_DURATION) {
            param(TIME_SPENT, durationAsString(preAssessmentDuration))
        }

        logEvent(PRE_MOOD_TRACKING_COMPLETION){
            param(IS_COMPLETED , Temp.completion.toString() )
        }

        if (Temp.completion){
            Temp.dailyProgramEngagement =  preAssessmentDuration
        }else{
            logEvent(DailyProgramEngagement) {
            param(TIME_SPENT, durationAsString(preAssessmentDuration))
              }
            logEvent(STOPPED_AT) {
                param(STOPPED_AT , Temp.stoppedAt!! )
            }
        }



        Temp.stoppedAt = ""
        Temp.completion = false
    }



}