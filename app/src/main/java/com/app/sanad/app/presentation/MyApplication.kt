package com.app.sanad.app.presentation

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.exoplayer2.util.NotificationUtil
import dagger.hilt.android.HiltAndroidApp
import com.app.sanad.R
import com.app.sanad.util.AlarmReceiver
import com.app.sanad.util.CALENDER_CHANNEL_ID
import com.app.sanad.util.ENCOURAGEMENT_CHANNEL_ID
import com.app.sanad.util.SCHEDULING_TIME
import com.app.sanad.util.SessionManager
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.log
import java.util.Calendar
@HiltAndroidApp
class MyApplication: Application() , DefaultLifecycleObserver {

    lateinit var sharedPreferences: SharedPreferencesManager

    override fun onCreate()

    {
        super<Application>.onCreate()
        sharedPreferences = SharedPreferencesManager(applicationContext)
        scheduleDailyAlarm(applicationContext)
        createChannel(getString(R.string.encouragement_messages),
            ENCOURAGEMENT_CHANNEL_ID, "", NotificationUtil.IMPORTANCE_DEFAULT)
        createChannel(getString(R.string.activities_reminder),
            CALENDER_CHANNEL_ID, "", NotificationUtil.IMPORTANCE_DEFAULT)
        ProcessLifecycleOwner.Companion.get().lifecycle.addObserver(this)
    }


    override fun onStart(owner: LifecycleOwner) {
        SessionManager.onAppForeground(context = applicationContext)
    }

    override fun onStop(owner: LifecycleOwner) {
        log("onAppBackground -> end onStop ")

        SessionManager.onAppBackground(context = applicationContext)
    }

    private fun createChannel(
        name: String,
        channelId: String,
        descriptionText: String,
        importance: Int
    ) {
        val mChannel = NotificationChannel(channelId, name, importance)
        mChannel.description = descriptionText
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }


    @SuppressLint("ScheduleExactAlarm")
    fun scheduleDailyAlarm(context: Context) {
        val schedulingTime = sharedPreferences.getInt(key = SCHEDULING_TIME, defaultValue = 7)
        println(schedulingTime)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 14) // 5 PM
            set(Calendar.MINUTE, 30)
            set(Calendar.SECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
            }
    }


}