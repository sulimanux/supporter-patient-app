package com.app.sanad.users.supporter.profile.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.app.sanad.util.SharedPreferencesManager
import javax.inject.Inject

@HiltViewModel
class SupporterProfileViewModel @Inject constructor
    ( val sharedPreferences: SharedPreferencesManager ): ViewModel() {

        val user = sharedPreferences.getUserProfile()

}