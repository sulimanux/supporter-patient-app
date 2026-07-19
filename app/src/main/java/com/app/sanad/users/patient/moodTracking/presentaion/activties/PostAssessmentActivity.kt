package com.app.sanad.users.patient.moodTracking.presentaion.activties

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseActivity
import com.app.sanad.util.DailyProgramEngagement
import com.app.sanad.util.IS_COMPLETED
import com.app.sanad.util.POST_ASSESSMENT_COMPLETED
import com.app.sanad.util.POST_ASSESSMENT_ENGAGEMENT
import com.app.sanad.util.POST_SELECTED_MOOD
import com.app.sanad.util.PRE_SELECTED_MOOD
import com.app.sanad.util.SELECTED_MOOD
import com.app.sanad.util.SUGGESTIONS
import com.app.sanad.util.TIME_SPENT
import com.app.sanad.util.Temp
import com.app.sanad.util.durationAsString

@AndroidEntryPoint
class PostAssessmentActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_assessment)
    }



    override fun onStop() {
        super.onStop()
        val durationAsLong = durationAsLong()
        trackPostAssessment()
        trackRecommendations()
        trackModeAfterAndBefore()

        logEvent(DailyProgramEngagement) {
            param(TIME_SPENT, durationAsString(durationAsLong + Temp.dailyProgramEngagement))
        }


        Temp.stoppedAt = ""
        Temp.completion = false
    }

    private fun trackPostAssessment() {
        logEvent(POST_ASSESSMENT_ENGAGEMENT) { param(TIME_SPENT, Temp.postAssessmentEngagement) }
        logEvent(POST_ASSESSMENT_COMPLETED) {
            param(
                IS_COMPLETED, Temp.isPostAssessmentCompleted.toString()
            )
        }
    }

    private fun trackRecommendations() {
        if (Temp.recommendationsEngagement != "") {
            logEvent(SUGGESTIONS) { param(TIME_SPENT, Temp.recommendationsEngagement) }
            Temp.recommendationsEngagement = ""
        }
    }

    private fun trackModeAfterAndBefore() {
        if (Temp.pre_mode_selected != "" && Temp.post_mode_selected != "") {
            logEvent(PRE_SELECTED_MOOD) {
                param(SELECTED_MOOD, Temp.pre_mode_selected)
            }
            logEvent(POST_SELECTED_MOOD) {
                param(SELECTED_MOOD, Temp.post_mode_selected)
            }
            Temp.pre_mode_selected = ""
            Temp.post_mode_selected = ""
        }
    }


}