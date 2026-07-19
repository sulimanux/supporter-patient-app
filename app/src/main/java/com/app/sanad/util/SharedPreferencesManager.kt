package com.app.sanad.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject
import androidx.core.content.edit
import com.app.sanad.auth.data.entity.Partner
import com.app.sanad.auth.data.entity.UserProfile

class SharedPreferencesManager @Inject constructor(context: Context) {

    private val PREFS_NAME = "MyAppPrefs"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun storeString(key: String, value: String?) {
        if (value != null) {
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
        }
    }
    fun storeInt(key: String, value: Int?) {
        if (value != null) {
            val editor = sharedPreferences.edit()
            editor.putInt(key, value)
            editor.apply()
        }
    }



    fun getString(key: String, defaultValue: String = "")= sharedPreferences.getString(key, defaultValue) ?: defaultValue

    fun getInt(key: String, defaultValue: Int = 0)=   sharedPreferences.getInt(key, defaultValue)

    fun storeBoolean(key: String, value: Boolean?) {
        if (value != null){
            val editor = sharedPreferences.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }


    fun clearData() {
        sharedPreferences.edit {
            clear()
        }
    }
    fun storeObject(key:String,obj: Any?) {
        if (obj != null) {
            sharedPreferences.edit {
                val gson = Gson()
                val json: String = gson.toJson(obj)
                putString(key, json)
            }
        }
    }

    fun getUserProfile() : UserProfile {
        val string = sharedPreferences.getString(USER_PROFILE, null.toString())
        val gson = Gson()
        return gson.fromJson(string, UserProfile::class.java)
    }

    fun getPartnerProfile() : Partner {
        val string = sharedPreferences.getString(PARTNER_PROFILE, null.toString())
        val gson = Gson()
        return gson.fromJson(string, Partner::class.java)
    }






}
