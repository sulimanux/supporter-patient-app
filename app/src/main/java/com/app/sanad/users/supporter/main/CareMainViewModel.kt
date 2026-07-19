package com.app.sanad.users.supporter.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.app.sanad.notifications.data.repos.NotificationsRepo
import com.app.sanad.posts.data.repo.PostsRepo
import com.app.sanad.util.SharedPreferencesManager
import javax.inject.Inject

@HiltViewModel
class CareMainViewModel @Inject constructor(
     val sharedPreferencesManager: SharedPreferencesManager,
     val firestore: FirebaseFirestore,
    private  val notificationsRepo: NotificationsRepo
) : ViewModel() {
    fun user() = sharedPreferencesManager.getUserProfile()
    val unReadCountNotification = notificationsRepo.unreadCountNotification

}