// Package responsible for presenting coffee idea tools for patient users
package com.app.sanad.users.patient.tools.coffeeideas.presentaion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.app.sanad.auth.data.entity.UserProfile
import com.app.sanad.users.supporter.tools.cofe.data.entity.UserIdea
import com.app.sanad.users.patient.supporters.data.repos.SupportersRepo
import com.app.sanad.users.patient.tools.coffeeideas.data.CofeRepo
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.log
import javax.inject.Inject

/**
 * ViewModel responsible for managing Coffee Ideas flow:
 * - Handling user input
 * - Managing questions & answers
 * - Sharing ideas with supporters or partners
 * - Listening to idea updates
 */
@HiltViewModel
class CofeViewModel @Inject constructor(
    val sharedPreferences: SharedPreferencesManager,
    private val supportersRepo: SupportersRepo,
    private val coffeeRepo: CofeRepo
) : ViewModel() {

    // Keeps track of current cup/question index
    var cupNumber = 0

    // Main idea text entered by the user
    private val _textIdea = MutableLiveData<String?>()
    var textIdea: MutableLiveData<String?> = _textIdea

    // Supporters and partner profiles exposed from repository
    val supportersProfile = supportersRepo.supportersProfile
    val partnerProfile = supportersRepo.partnerProfile

    // State to indicate success/failure of sharing actions
    private val _sharingState = MutableLiveData<Boolean?>()
    var sharingState: MutableLiveData<Boolean?> = _sharingState

    // Reset sharing state after UI consumes it
    fun resetSharingState() {
        _sharingState.value = null
    }

    // Returns current logged-in user profile
    fun user() = sharedPreferences.getUserProfile()

    // User-adjusted idea text after edits
    private val _userAdjustedText = MutableLiveData<String?>()
    var userAdjustedText: MutableLiveData<String?> = _userAdjustedText

    // Questions answered by the user
    private val _textQuestion1 = MutableLiveData<String?>()
    var textQuestion1: MutableLiveData<String?> = _textQuestion1

    private val _textQuestion2 = MutableLiveData<String?>()
    var textQuestion2: MutableLiveData<String?> = _textQuestion2

    private val _textQuestion3 = MutableLiveData<String?>()
    var textQuestion3: MutableLiveData<String?> = _textQuestion3

    private val _textQuestion4 = MutableLiveData<String?>()
    var textQuestion4: MutableLiveData<String?> = _textQuestion4

    private val _textQuestion5 = MutableLiveData<String?>()
    var textQuestion5: MutableLiveData<String?> = _textQuestion5

    // Live idea object coming from repository
    val userIdea = coffeeRepo.userIdea

    /**
     * Retrieve all supporters linked to the current user
     */
    fun retrieveSupporters() {
        log("retrieveSupporters")
        viewModelScope.launch {
            supportersRepo.retrievePartnersIds(supportersRepo.userProfile().id!!)
        }
    }

    /**
     * Clears all idea-related data and resets flow
     */
    fun clearData() {
        _textIdea.value = null
        _userAdjustedText.value = null
        _textQuestion1.value = null
        _textQuestion2.value = null
        _textQuestion3.value = null
        _textQuestion4.value = null
        _textQuestion5.value = null
        cupNumber = 0
    }

    /**
     * Clears cached supporters profile
     */
    fun resetSupporters() {
        supportersProfile.value = null
    }

    /**
     * Checks whether all required questions have been answered
     */ // (true and false and true and true  ) == false
    fun isAllQuestionsAnswered(): Boolean  {
    log("${_textQuestion1.value}")
        log("${_textQuestion2.value}")
        log("${_textQuestion3.value}")
        log("${_textQuestion4.value}")
        log("${_textQuestion5.value}")
        return         !_textQuestion1.value.isNullOrEmpty()
                && !_textQuestion2.value.isNullOrEmpty()
                && !_textQuestion3.value.isNullOrEmpty()
                && !_textQuestion4.value.isNullOrEmpty()
                && !_textQuestion5.value.isNullOrEmpty()
    }


    /**
     * Shares a user idea with a selected supporter
     */
    fun shareIdeaWithSupporter(idea: UserIdea, supporter: UserProfile) {
        try {
            viewModelScope.launch {
                coffeeRepo.shareIdeaWithSupporter(idea, supporter)
                _sharingState.value = true
            }

        } catch (e: Exception) {
            _sharingState.value = false
        }
    }

    /**
     * Retrieves a supporter using a temporarily saved supporter ID
     */
    fun retrieveSupporter() {
        try {
            val id = sharedPreferences.getString("tempSupporterId")
            viewModelScope.launch {
                supportersRepo.retrievePartner(id)
            }
        } catch (e: Exception) {
            // Silent failure
        }
    }

    /**
     * Retrieves the user's assigned partner
     */
    fun retrievePartner() {
        try {
            val id = user().partnerId!!
            viewModelScope.launch {
                supportersRepo.retrievePartner(id)
            }
        } catch (e: Exception) {
            // Silent failure
        }
    }

    /**
     * Starts listening for realtime updates to the idea
     */
    fun listenToIdeaChanges() {
        try {
            viewModelScope.launch {
                coffeeRepo.listenToIdeaChanges()
            }
        } catch (e: Exception) {
        }
    }

    /**
     * Marks an idea as seen by the user
     */
    fun updateSeenByUser(key: String) {
        try {
            viewModelScope.launch {
                coffeeRepo.updateSeenByUser(key)
            }
        } catch (e: Exception) {

        }
    }

    /**
     * Sends supporter response back to repository
     */
    fun sendSupporterResponse(idea: UserIdea) {
        try {
            viewModelScope.launch {
                coffeeRepo.sendSupporterResponse(idea)
                _sharingState.value = true
            }
        } catch (e: Exception) {
            _sharingState.value = false
        }
    }
}
