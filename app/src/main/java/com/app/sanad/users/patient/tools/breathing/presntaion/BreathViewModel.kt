package com.app.sanad.users.patient.tools.breathing.presntaion

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.app.sanad.users.patient.tools.breathing.data.repo.BreathingRepo
import com.app.sanad.util.log
import javax.inject.Inject

/**
 * ViewModel for handling breathing exercise logic, countdown timer, durations, and sounds
 */
@HiltViewModel
class BreathViewModel @Inject constructor(
    private val breathingRepo: BreathingRepo,
) : ViewModel() {

    // Selected duration (number of repetitions)
    var currentDuration = 0

    // Selected sound ID
    var soundId = 0

    // Internal counter for tracking current repetition
    private var counter: Int = 0

    // LiveData for remaining milliseconds in the current countdown
    private val _progressState = MutableLiveData<Long?>()
    val progressState: LiveData<Long?> get() = _progressState

    // LiveData for whether the timer is currently running
    private val _isTimerRunning = MutableLiveData<Boolean>()
    val isTimerRunning: LiveData<Boolean> get() = _isTimerRunning

    // LiveData for triggering sound changes
    private val _changeSound = MutableLiveData<Boolean>()
    val changeSound: LiveData<Boolean> get() = _changeSound

    // LiveData to trigger progress reset in UI
    private val _resetProgress = MutableLiveData<Boolean>()
    val resetProgress: LiveData<Boolean> get() = _resetProgress

    // LiveData to show a dialog (e.g., when user clicks start while timer is running)
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> get() = _showDialog

    // LiveData to show the duration text (e.g., "10 min")
    private val _textDuration = MutableLiveData<String>()
    val textDuration: LiveData<String> get() = _textDuration

    // LiveData for remaining seconds in the current phase
    private val _remainingTime = MutableLiveData<Int>()
    val remainingTime: LiveData<Int> get() = _remainingTime

    // Sets the text to display the selected duration
    fun setTextDuration(string: String) {
        _textDuration.value = string
    }

    // Triggers the sound change observer
    fun setChangeSound(restart: Boolean) {
        _changeSound.value = restart
    }

    // CountDownTimer instance
    private var countdownTimer: CountDownTimer? = null

    // Returns list of available durations from repository
    fun listOfDurations(context: Context) = breathingRepo.listOfDurations(context)

    // Returns list of available sounds from repository
    fun listOfSounds(context: Context) = breathingRepo.listOfSounds(context)

    /**
     * Called when the Start button is clicked
     * Starts the countdown if timer not running, or shows dialog if already running
     */
    /// counter = 0
    /// D = > 5
    // start
    // counter = 0
    /// counter == 1

    fun onStartButtonClicked() {
        counter = 0
        if (_isTimerRunning.value == true) {
            _showDialog.value = true
        } else {
            startCountdown(getSelectedDurationInMillis())
        }
    }

    // Returns selected duration in milliseconds (currently fixed to 1 minute)
    fun getSelectedDurationInMillis(): Long {
        return 1 * 60 * 1000L
    }

    /**
     * Starts the countdown timer for the selected duration
     * Recursively starts next repetition until currentDuration is reached
     */
    private fun startCountdown(selectedDurationInMillis: Long) {
        log("startCountdown $counter $$ $currentDuration  $$ $soundId")
        counter++  // Increment repetition counter
        _progressState.value = selectedDurationInMillis
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(selectedDurationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _progressState.value = millisUntilFinished
                calcRemainingTime(millisUntilFinished)
            }
            override fun onFinish() {
                resetProgress()
                resetRemainingTime()
                _progressState.value = 0
                if (counter < currentDuration) {
                    startCountdown(getSelectedDurationInMillis())
                }
            }
        }.start()
        _isTimerRunning.value = true
    }

    /**
     * Calculates remaining seconds from milliseconds and updates LiveData
     */
    private fun calcRemainingTime(millisUntilFinished: Long) {
        val secondsRemaining = (millisUntilFinished / 1000).toInt() // 59
        _remainingTime.value = secondsRemaining // 59
    }

    /**
     * Cancels the countdown timer
     */
    fun cancelCountdown() {
        countdownTimer?.cancel()
    }

    /**
     * Resets timer running state
     */
    fun resetIsTimerRunning() {
        _isTimerRunning.value = false
    }

    /**
     * Clean up timer when ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        countdownTimer?.cancel()
    }



    /**
     * UI control functions
     */
    fun resetProgress() { _resetProgress.value = true }
    fun resetRestProgress() { _resetProgress.value = false }
    fun resetRemainingTime() { _remainingTime.value = 0 }
    fun restShowDialog() { _showDialog.value = false }

    /**
     * Clears all data and cancels ongoing timers
     */
    fun clearData() {
        cancelCountdown()
        resetRemainingTime()
        resetIsTimerRunning()
        _progressState.value = null
    }
}
