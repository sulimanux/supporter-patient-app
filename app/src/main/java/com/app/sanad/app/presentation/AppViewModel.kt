package com.app.sanad.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.app.sanad.notifications.data.repos.NotificationsRepo
import com.app.sanad.posts.data.repo.PostsRepo
import com.app.sanad.users.patient.tools.coffeeideas.data.CofeRepo
import com.app.sanad.users.patient.tools.coffeeideas.presentaion.CofeViewModel
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.log
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val notificationsRepo: NotificationsRepo,
      val postsRepo: PostsRepo,
    private val sharedPreferencesManager: SharedPreferencesManager
): ViewModel() {


    fun setUpListeners() {
        log("AppViewModel setUpListeners start")
        viewModelScope.launch {
            notificationsRepo.setNotificationsListener()
            postsRepo.addListenerToPosts()
        }

    }




}