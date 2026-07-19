package com.app.sanad.users.patient.dailyprogram.presentaion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.app.sanad.users.patient.dailyprogram.data.repo.DailyProgramRepository
import com.app.sanad.users.patient.dailyprogram.data.entity.StatusDailyProgram
import com.app.sanad.users.patient.dailyprogram.data.entity.Task
import com.app.sanad.util.SharedPreferencesManager
import javax.inject.Inject

@HiltViewModel
class DailyProgramViewModel @Inject constructor(
    private val dailyProgramRepository: DailyProgramRepository,
    val sharedPreferences: SharedPreferencesManager,
) : ViewModel() {

    // Get current user's profile
    fun userProfile() = dailyProgramRepository.getUserProfile()

    // Status of the current day
    lateinit var status: StatusDailyProgram

    // List of tasks for the current day
    lateinit var listOfTasks: List<Task>

    // Flag to track if local changes need remote sync
    var isSyncNeeded = false

    // Fetch current day data from local storage
    private fun currentDayLocal() = dailyProgramRepository.getCurrentDayLocally()

    // Initialize task list based on the phase (educational, spiritual, or behaviorActivation)
    fun initTasksList(phase: String) {
        status = currentDayLocal().status!!

        listOfTasks = when (phase) {
            "educational" -> currentDayLocal().dayTask?.educational as List<Task>
            "spiritual" -> currentDayLocal().dayTask?.spiritual as List<Task>
            else -> currentDayLocal().dayTask?.behaviorActivation as List<Task>
        }
    }

    // Update completion rate after a task is done
    fun updateCompletionRate() {
        status.remaining = status.remaining?.minus(1)

        // Increase completion rate based on religion
        if (userProfile().religion!!) {
            status.completionRate = status.completionRate?.plus(30)
        } else {
            status.completionRate = status.completionRate?.plus(50)
        }

        updateCurrentTaskLocally()
    }

    // Save current day status and tasks locally
    fun updateCurrentTaskLocally() {
        val currentDay = currentDayLocal()
        currentDay.status = status
        dailyProgramRepository.updateCurrentDayLocally(currentDay)
        isSyncNeeded = true
    }

    // Sync current day status and tasks to remote server
    fun updateCurrentTaskRemotely() {
        viewModelScope.launch {
            dailyProgramRepository.updateCurrentDayRemotely(currentDayLocal())
            isSyncNeeded = false
        }
    }
}
