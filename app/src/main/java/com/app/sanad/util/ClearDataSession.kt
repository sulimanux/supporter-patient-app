package com.app.sanad.util

import com.app.sanad.chatting.data.repo.ChattingRepo
import com.app.sanad.users.patient.moodTracking.data.repo.MoodTrackingRepository
import javax.inject.Inject

class ClearDataSession @Inject constructor(
    private val moodTrackingRepository: MoodTrackingRepository,
   private  val chattingRepo: ChattingRepo,
) {




    fun clearData(){
        moodTrackingRepository.clear()
        chattingRepo.clearData()
        UserDataListener.removeListener()
    }
}