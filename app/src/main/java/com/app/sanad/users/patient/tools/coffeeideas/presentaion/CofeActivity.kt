package com.app.sanad.users.patient.tools.coffeeideas.presentaion

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import com.app.sanad.R
import com.app.sanad.base.BaseActivity
import com.app.sanad.util.INTRO1
import com.app.sanad.util.INTRO_ENGAGEMENT
import com.app.sanad.util.INTRO_VIEWED
import com.app.sanad.util.THOUGHTS_RESTRUCTURING_ENGAGEMENT
import com.app.sanad.util.TIME_SPENT
import com.app.sanad.util.Temp
import com.app.sanad.util.durationAsString
import com.app.sanad.util.log

@AndroidEntryPoint
class CofeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cofe)
    }


    override fun onStop() {
        super.onStop()
        logEvent (THOUGHTS_RESTRUCTURING_ENGAGEMENT){
            param(TIME_SPENT , durationAsString(durationAsLong()))
        }
        isIntroViewed()

    }

    private fun isIntroViewed() {
        val isViewed = if (Temp.stoppedAt != INTRO1 && Temp.stoppedAt != "IntroFragment") "true" else "false"
        logEvent(INTRO_VIEWED){
            param(INTRO_VIEWED, isViewed)
        }
      logEvent (INTRO_ENGAGEMENT){
          param(TIME_SPENT , Temp.introEngagement)
      }
      Temp.introEngagement= ""
    }


}