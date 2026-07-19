package com.app.sanad.users.patient.main.presentaion

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import com.app.sanad.users.patient.dailyprogram.data.entity.CurrentDay
import com.app.sanad.users.patient.main.data.UserDataRepository
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.log
import javax.inject.Inject
@HiltViewModel
 class UserViewModel @Inject constructor(
    val sharedPreferencesManager: SharedPreferencesManager,
      val firestore: FirebaseFirestore,
     private  val userDataRepository: UserDataRepository,
) : ViewModel() {


    val userProfile = userDataRepository.userProfile()


    fun currentTask() : CurrentDay {
        val day = userDataRepository.getCurrentDayLocally()
        log("day = $day")
        return day


    }

    fun updateFirstTimeState() {
        userDataRepository.updateLoggedStatus()
    }

    fun isFirstTime ()= userDataRepository.isFirstTime()



}

