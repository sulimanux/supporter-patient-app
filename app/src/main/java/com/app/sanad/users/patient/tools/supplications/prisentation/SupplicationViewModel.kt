// Package for supplications presentation layer ViewModel
package com.app.sanad.users.patient.tools.supplications.prisentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.app.sanad.posts.data.entity.Post
import com.app.sanad.model.Supplication
import com.app.sanad.users.patient.supporters.data.repos.SupportersRepo
import com.app.sanad.users.patient.tools.supplications.data.SupplicationsRepo
import com.app.sanad.util.SUPPLICATIONS
import com.app.sanad.util.log
import javax.inject.Inject

/**
 * ViewModel responsible for:
 * - Managing supplication data (suggested & user)
 * - Handling counting logic and images
 * - Creating supplication posts for sharing
 */
@HiltViewModel
class SupplicationViewModel @Inject constructor(

    // Repository for supplications data
    val supplicationsRepo: SupplicationsRepo,

    // Repository for supporters data
    private val supportersRepo: SupportersRepo,

    ) : ViewModel() {

    // Controls dismissing the Add Supplication dialog
    private val _dismissSupplicationDialog = MutableLiveData<Boolean>()
    val dismissSupplicationDialog: LiveData<Boolean>
        get() = _dismissSupplicationDialog

    // Current logged-in user
    val user = supplicationsRepo.getUser()

    // Selected supplication for details screen
    var selectedSupplication: Supplication? = null

    // Suggested supplications provided by the app
    private val _suggestedSupplication = MutableLiveData<List<Supplication>>()
    val suggestedSupplication: LiveData<List<Supplication>>
        get() = _suggestedSupplication

    // Supplications created by the user
    private val _userSupplications = MutableLiveData<List<Supplication>>()
    val userSupplications: LiveData<List<Supplication>>
        get() = _userSupplications

    // List of images used during counting (hand / sebha)
    private var mListImages = supplicationsRepo.handsList()
    private var currentIndexListImages = 0

    // Remaining count of repetitions
    private val _numberRemaining = MutableLiveData<Int>()
    val numberRemaining: LiveData<Int>
        get() = _numberRemaining

    // Emits the next image to be displayed
    private val _newImageSupplication = MutableLiveData<Int>()
    val newImageSupplication: LiveData<Int>
        get() = _newImageSupplication

    /**
     * Initializes listeners for user and app supplications
     */
    init {
        supplicationsRepo.listenToUserSupplications {
            _userSupplications.value = it
        }

        supplicationsRepo.listenToAppSupplications {
            _suggestedSupplication.value = it
        }
    }

    /**
     * Updates the current image list (hand or sebha)
     */
    fun setListImage(listImages: List<Int>) {
        mListImages = listImages
    }

    /**
     * Resets dialog dismiss state after it has been handled
     */
    fun resetDismissSupplicationDialog() {
        _dismissSupplicationDialog.value = false
    }

    /**
     * Stores a new user supplication and triggers dialog dismissal
     */
    fun storeUserSupplication(newSupplication: Supplication) {
        viewModelScope.launch {
            try {
                supplicationsRepo.storeUserSupplication(newSupplication)
                _dismissSupplicationDialog.value = true
            } catch (e: Exception) {
                _dismissSupplicationDialog.value = false
            }
        }
    }

    /**
     * Updates an existing user supplication and triggers dialog dismissal
     */
    fun updateUserSupplication(supplication: Supplication) {
        viewModelScope.launch {
            try {
                supplicationsRepo.updateUserSupplication(supplication)
                _dismissSupplicationDialog.value = true
            } catch (e: Exception) {
                _dismissSupplicationDialog.value = false
            }
        }
    }

    /**
     * Deletes a user supplication
     */
    fun deleteUserSupplication(supplicationId: String) {
        viewModelScope.launch {
            try {
                supplicationsRepo.deleteUserSupplication(supplicationId)
            } catch (e: Exception) {
                log("Failed to delete supplication: ${e.message}")
            }
        }
    }

    /**
     * Creates a Post object for sharing a supplication
     */
    fun post(supporters: MutableList<String>, listId:MutableList<String>) =
        Post(
            listSupportersId = listId,
            type = SUPPLICATIONS,
            supplication = selectedSupplication,
            supporters = supporters
        )

    /**
     * Updates image and counter for each click
     */
    private fun getImage() {
        if (currentIndexListImages == mListImages.size - 1) {
            currentIndexListImages = 0
        }

        currentIndexListImages++
        _newImageSupplication.value = mListImages[currentIndexListImages]
        _numberRemaining.value = _numberRemaining.value?.plus(1)
    }

    /**
     * Called when user clicks on the hand/sebha image
     * Controls counting logic based on target number
     */
    fun onHandClick() {
        log("onHandClick")
        log(_numberRemaining.value.toString())

        if (selectedSupplication?.number == 0) {
            getImage()
        } else {
            if (_numberRemaining.value!! < selectedSupplication?.number!!) {
                getImage()
            }
        }
    }

    /**
     * Resets counter and image to initial state
     */
    fun resetCounter() {
        _numberRemaining.value = 0
        currentIndexListImages = 0
        getFirstImage()
    }

    /**
     * Displays the first image in the list
     */
    private fun getFirstImage() {
        _newImageSupplication.value = mListImages[currentIndexListImages]
    }
}
