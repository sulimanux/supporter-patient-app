package com.app.sanad.base

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import com.app.sanad.chatting.data.repo.ChattingRepo
import com.app.sanad.util.ClearDataSession
import com.app.sanad.util.IS_SECOND_TIME
import com.app.sanad.util.LANGUAGE
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.USER_EMAIL
import com.app.sanad.util.log
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    private val fireAnalytics: FirebaseAnalytics,
    private val sharedPreferences: SharedPreferencesManager,
    private val firebaseAuth: FirebaseAuth,
    val chattingRepo: ChattingRepo,
    private  val clearDataSession: ClearDataSession,

) : ViewModel() {

    fun currentUserProfile() = sharedPreferences.getUserProfile()

    fun updateUserPropertyAnalytics() {
        val userId = currentUserProfile().id
        currentUserProfile().name
//        fireAnalytics.setUserProperty("user_name", name)
        fireAnalytics.setUserId(userId)



    }



    fun logOut(context: Context) {

        val result = context.deleteDatabase("database-name")
        if (result) {
            log("database deleted")
        } else {
            log("database not deleted")
        }
        clearDataSession.clearData()

        firebaseAuth.signOut()
        val email = sharedPreferences.getString(USER_EMAIL)
        val currentLang = sharedPreferences.getString(LANGUAGE)
        sharedPreferences.clearData()
        sharedPreferences.storeBoolean(IS_SECOND_TIME, true)

        sharedPreferences.storeString(
            LANGUAGE, currentLang
        )
        sharedPreferences.storeString(
            USER_EMAIL, email
        )
    }
}