package com.app.sanad.users.patient.tools.breathing.data.repo

import android.content.Context
import com.app.sanad.R
import com.app.sanad.users.patient.tools.breathing.data.entity.Duration
import com.app.sanad.users.patient.tools.breathing.data.entity.Sound

class BreathingRepo {

    fun listOfDurations(context: Context) = getListOfDurations(context)
    fun listOfSounds(context: Context) = getSoundsList(context)
}


fun getListOfDurations(context: Context) = listOf(
    Duration(context.getString(R.string.minutes_1),context.getString(R.string.minutes_1_description) , 1, R.drawable.min5 ),
    Duration(context.getString(R.string.minutes_2),context.getString(R.string.minutes_2_description), 2, R.drawable.min5),
    Duration(context.getString(R.string.minutes3),context.getString(R.string.minutes_3_description), 3, R.drawable.min5),
    Duration(context.getString(R.string.minutes_4),context.getString(R.string.minutes_4_description), 4, R.drawable.min5),
    Duration(context.getString(R.string.minutes_5),context.getString(R.string.minutes_5_description), 5, R.drawable.min5),
)

fun getSoundsList(context: Context) =
    listOf(
        Sound(context.getString(R.string.sound_sea) ,  R.raw.sea4,  R.drawable.img_sea ),
        Sound(context.getString(R.string.sound_rain) , R.raw.rain4 , R.drawable.image_rain),
        Sound(context.getString(R.string.sound_air) , R.raw.air , R.drawable.air),
        Sound(context.getString(R.string.sound_birds) , R.raw.bird , R.drawable.birds),
        Sound(context.getString(R.string.no_sound) , 0 , R.drawable.no_sound),
    )