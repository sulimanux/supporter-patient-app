package com.app.sanad.users.patient.profile.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.app.sanad.users.patient.dailyprogram.data.repo.DailyProgramRepository
import com.app.sanad.users.patient.profile.data.ProfileRepo
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.log
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    val sharedPreferences: SharedPreferencesManager,
    private val profileRepo: ProfileRepo,
    private val dailyProgramRepo: DailyProgramRepository,

) : ViewModel() {

    /**
     * Retrieves the user's profile from the repository.
     *
     * @return UserProfile object.
     */
    fun userProfile() = profileRepo.userProfile()

    // LiveData to track the status of asynchronous operations (e.g., success or failure).
    private val _status = MutableLiveData<Boolean?>()
    val status: LiveData<Boolean?> = _status

    /**
     * Resets the status LiveData to null.
     */
    fun restStatus() {
        _status.value = null
    }

    /**
     * Uploads a user's profile image to Firebase Storage.
     *
     * @param uri The URI of the image to upload.
     */
    fun uploadImageToFireStorage(uri: Uri) {
        viewModelScope.launch {
            try {
                profileRepo.uploadImageToFireStorage(uri)
                _status.value = true // Indicate success
            } catch (e: Exception) {
                _status.value = false // Indicate failure
            }
        }
    }

    /**
     * Updates a specific field in the user's profile remotely.
     *
     * @param key The key of the profile field to update.
     * @param value The new value for the field.
     */
    fun updateUserProfileRemotely(key: String, value: Any) {
        viewModelScope.launch {
            try {
                profileRepo.updateUserProfileRemotely(key, value)
                _status.value = true // Indicate success
            } catch (e: Exception) {
                _status.value = false // Indicate failure
            }
        }
    }

    /**
     * Changes the user's password after re-authenticating them.
     *
     * @param currentPassword The user's current password for re-authentication.
     * @param newPassword The new password to set.
     */
    fun changeUserPassword(currentPassword: String, newPassword: String) {
        val fireAuth = FirebaseAuth.getInstance()
        val currentUser = fireAuth.currentUser
        val credential = EmailAuthProvider.getCredential(userProfile().email!!, currentPassword)

        viewModelScope.launch {
            try {
                // Re-authenticate the user before changing the password
                currentUser?.reauthenticate(credential)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // If re-authentication is successful, update the password
                        currentUser.updatePassword(newPassword).addOnCompleteListener { task ->
                            _status.value = task.isSuccessful
                        }
                    } else {
                        // If re-authentication fails, log the error and indicate failure
                        log(task.exception.toString() + "   ele")
                        _status.value = false
                    }
                }?.await()
            } catch (e: Exception) {
                _status.value = false // Indicate failure
            }
        }
    }

    /**
     * Updates the user's religion and resets their current daily program.
     *
     * @param key The key for the religion field.
     * @param value The new religion value.
     */
    fun updateReligion(key: String, value: Any) {
        viewModelScope.launch {
            try {
                profileRepo.updateUserProfileRemotely(key, value)
                log("4")
                resetCurrentDay() // Reset the daily program after updating religion
                log("7")
                _status.value = true // Indicate success
            } catch (e: Exception) {
                _status.value = false // Indicate failure
            }
        }
    }

    /**
     * Resets the user's current daily program by advancing to the next day.
     */
    private suspend fun resetCurrentDay() {
        log("5")
        val userProfile = userProfile()
        log(userProfile.religion!!.toString())
        dailyProgramRepo.getNextDay(userProfile.currentDay!!)
        log("6")
    }
}