package com.app.sanad.util

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.runBlocking
import com.app.sanad.R
import com.app.sanad.app.data.AppDatabase
import com.app.sanad.users.patient.main.presentaion.UserScreensActivity
import java.util.Date

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        runBlocking {
            val today = CalendarDay.today()
            val database = AppDatabase.getDatabase(context = context)
            val days = database.dayDao().getAllDays()
            val list = mutableSetOf<CalendarDay>()

            days?.forEach { day ->
                val date = Date(day.day)
                val calendarDay = CalendarDay(date)
                list.add(calendarDay)
            }

            if (list.contains(today)){
                sendNotification("لا تنسى القيام بمهام اليوم" , context)
            }
        }

    }

    @SuppressLint("MissingPermission")

    fun sendNotification(text:String,context: Context){
        val intent = Intent(context, UserScreensActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, CALENDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("My Application")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(15 ,builder.build())

    }

}
