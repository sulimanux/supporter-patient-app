package com.app.sanad.users.patient.tools.gratitude.presentaion

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.app.sanad.users.patient.tools.gratitude.data.entity.Gratitude
import com.app.sanad.users.patient.tools.gratitude.data.repo.GratitudeRepo
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GratitudeViewModel @Inject constructor(
    val gratitudeRepo: GratitudeRepo
) : ViewModel() {

    // Exposed gratitude list observable
    val gratitudeList = gratitudeRepo.gratitudeList

    // Holds the currently selected question index
    private var _selectedPosition: Int = 0

    /**
     * Returns the currently selected question position
     */
    fun getSelectedPosition() = _selectedPosition

    /**
     * Returns a random gratitude question and updates selected position
     */
    fun getRandomQuestion(context: Context): String {
        val questions = gratitudeRepo.getGratitudeQuestionsList(context)

        // Generate random index within question list range
        val randomNumber = Random.nextInt(questions.size.minus(1))
        _selectedPosition = randomNumber

        return questions[randomNumber]
    }

    /**
     * Returns a gratitude question by index
     */
    fun getQuestion(context: Context, index: Int): String {
        val questions = gratitudeRepo.getGratitudeQuestionsList(context)
        return questions[index]
    }

    /**
     * Updates the selected question position manually
     */
    fun setSelectedPosition(randomNumber: Int) {
        _selectedPosition = randomNumber
    }

    /**
     * Saves gratitude remotely and returns result via callback
     */
    fun saveGratitudeRemotely(
        gratitude: Gratitude,
        callBack: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                gratitudeRepo.saveGratitudeRemotely(gratitude)
                callBack(true)
            } catch (e: Exception) {
                callBack(false)
            }
        }
    }

    /**
     * Retrieves gratitude list from remote source
     */
    fun retrieveGratitudeListRemotely() {
        viewModelScope.launch {
            try {
                gratitudeRepo.retrieveGratitudeListRemotely()
            } catch (e: Exception) {
                // Emit empty list in case of failure
                gratitudeList.value = emptyList()
            }
        }
    }
}
