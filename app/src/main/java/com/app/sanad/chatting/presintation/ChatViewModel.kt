package com.app.sanad.chatting.presintation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.app.sanad.chatting.data.repo.ChattingRepo
import com.app.sanad.chatting.data.entity.Message
import com.app.sanad.chatting.data.entity.MetaDataMessages
import com.app.sanad.users.patient.supporters.data.repos.SupportersRepo
import com.app.sanad.util.SharedPreferencesManager
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    val chattingRepo: ChattingRepo,                 // Chat data source
    val sharedPreferences: SharedPreferencesManager, // Local storage
    val supportersRepo: SupportersRepo     ,          // Supporters data

) : ViewModel() {

    // Messages of current chat
    val messages = chattingRepo.messagesList

    // Partners list
    val partners = supportersRepo.supportersProfile

    // Logged-in user
    val user = chattingRepo.user()

    // Trigger to clear message input
    private val _clearEditText = MutableLiveData<Boolean>()
    val clearEditText: LiveData<Boolean> = _clearEditText

    // Selected chat partner ID
    var partnerId = ""



    // Role check
    fun isUserSupporter() = chattingRepo.isUserSupporter()

    // Partner existence check
    fun hasPartner() = chattingRepo.hasPartner()!!

    // Listen to messages of selected partner
    fun listenToMessages() {
        viewModelScope.launch {
            try {
                chattingRepo.listenToMessages(partnerId)
            } catch (e: Exception) {
                // Intentionally ignored (bad practice)
            }
        }
    }

    // Fetch partners IDs
    fun retrievePartners() {
        viewModelScope.launch {
            try {
                supportersRepo.retrievePartnersIds(
                    supportersRepo.userProfile().id!!
                )
            } catch (e: Exception) {
                // Silent failure
            }
        }
    }

    // Send message and notify UI to clear input
    fun sendMessage(message: Message, metaDataMessages: MetaDataMessages) {
        viewModelScope.launch {
            try {
                chattingRepo.sendMessage(message, metaDataMessages, partnerId)
                _clearEditText.value = true
            } catch (e: Exception) {
                // Silent failure
            }
        }
    }

    // Reset clear-input flag
    fun resetClearEditText() {
        _clearEditText.value = false
    }

    // Clear messages when leaving chat
    fun clearMessages() {
        messages.value = null
    }

    fun updateSeenMessages(idPartner: String, index: Int) {

        viewModelScope.launch {
            try {
                chattingRepo.updateSeenMessages(idPartner, index)
            }catch (e: Exception){

            }
        }
    }
}
