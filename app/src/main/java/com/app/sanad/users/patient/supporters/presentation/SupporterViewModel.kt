package com.app.sanad.users.patient.supporters.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.app.sanad.R
import com.app.sanad.users.patient.supporters.data.repos.SupportersRepo
import com.app.sanad.util.STATUS
import com.app.sanad.util.USER_PROFILE
import com.app.sanad.util.log
import javax.inject.Inject


@HiltViewModel
class SupporterViewModel @Inject constructor(
    private val supportersRepo: SupportersRepo,
      )
    : ViewModel() {

   private val _status = MutableLiveData<String?>()
   var status: LiveData<String?> = _status

    private val _updateStatus = MutableLiveData<String?>()
    var updateStatus: LiveData<String?> = _updateStatus


    val supportersProfile = supportersRepo.supportersProfile


   fun userProfile()= supportersRepo.userProfile()
   fun getInstructionsList()= supportersRepo.instructionsList()
    fun currentLang () = supportersRepo.currentLang()


    fun retrieveSupporters(){
        viewModelScope.launch {
            try {
                    supportersRepo.retrievePartnersIds(userProfile().id!!)
            }catch (e:Exception){
                log("Exception")
               log(e.message.toString())
            }
        }
    }

    fun storeNewInvitationCode(newInvitationCode: String) {
        viewModelScope.launch {
            try {
            supportersRepo.storeNewInvitationCode(newInvitationCode)
            updateUserProfileLocal(newInvitationCode)
            } catch (e: Exception) {
                _status.value = e.message
            }
        }
    }

    private fun updateUserProfileLocal(newInvitationCode: String) {
        try {
            supportersRepo.updateUserProfileLocal(newInvitationCode)
            _status.value = ""
        }catch (e:Exception){
            _status.value = e.message
        }
      supportersRepo.sharedPreferences.storeObject(USER_PROFILE, userProfile())
    }



    fun changeStatusOfSupporter(supporterId:String,status: Int?) {
        val updateData = mapOf(
            STATUS to status,
        )
      viewModelScope.launch {
          try {
              supportersRepo.changeStatusOfSupporter(supporterId,updateData)
              _updateStatus.value = ""
          } catch (e: Exception) {
              _updateStatus.value = e.message
          }
      }

    }

    fun updateSupporterPermissionsRemotely(supporterId: String,modifiedPermissions: MutableList<Boolean>, context: Context) {
        val updatedPermissions = hashMapOf(
            "allowDailyProgramDetails" to modifiedPermissions[0],
            "allowMoodTrackingDetails" to modifiedPermissions[1],
            "allowPrivateMessages" to modifiedPermissions[2],
        )
        viewModelScope.launch {
            try {
                supportersRepo.updateSupporterPermissionsRemotely(supporterId,updatedPermissions)
                _status.value = context.getString(R.string.supporter_s_permission_have_been_updated)
            } catch (e: Exception) {
                _status.value = context.getString(R.string.failed_try_again_later)
    }
    }
    }

}