package com.app.sanad.users.patient.main.data

import com.google.gson.Gson
import com.app.sanad.users.patient.dailyprogram.data.entity.CurrentDay
import com.app.sanad.util.CURRENT_DAY
import com.app.sanad.util.IS_FIRST_TIME
import com.app.sanad.util.SharedPreferencesManager

class UserDataRepository (
    val sharedPreferences: SharedPreferencesManager,
){

     fun userProfile () = sharedPreferences.getUserProfile()


     fun getCurrentDayLocally(): CurrentDay {
        val string = sharedPreferences.getString(CURRENT_DAY, null.toString())
        val gson = Gson()
        return gson.fromJson(string, CurrentDay::class.java)
    }

    fun isFirstTime() = sharedPreferences.getBoolean(IS_FIRST_TIME)

    fun updateLoggedStatus() = sharedPreferences.storeBoolean(IS_FIRST_TIME,false)



}