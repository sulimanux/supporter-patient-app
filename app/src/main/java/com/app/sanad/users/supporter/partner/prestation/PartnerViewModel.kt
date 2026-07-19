package com.app.sanad.users.supporter.partner.prestation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.app.sanad.users.supporter.partner.data.PartnerRepo
import com.app.sanad.util.SharedPreferencesManager
import javax.inject.Inject

@HiltViewModel
class PartnerViewModel @Inject constructor(
  val sharedPreferences: SharedPreferencesManager,
  private val partnerRepo: PartnerRepo,
):ViewModel()
{

  fun partner() = partnerRepo.getPartnerLocally()
  val user = partnerRepo.user

}