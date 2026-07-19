/**
 * ViewModel managing daily mood tracking workflow.
 * Handles mood selections, storing daily mood records,
 * and syncing local and remote tracking data.
 */
package com.app.sanad.users.patient.moodTracking.presentaion.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.app.sanad.users.patient.moodTracking.data.repo.MoodTrackingRepository
import com.app.sanad.users.patient.moodTracking.data.entity.EmojiMood
import com.app.sanad.util.log
import javax.inject.Inject

@HiltViewModel
class MoodTrackingViewModel @Inject constructor(
    private val moodTrackingRepository: MoodTrackingRepository,
) : ViewModel() {

    private var preMoodIndex = -1
    private var postMoodIndex = -1
    private var _emoji: EmojiMood? = null

    val trackingList = moodTrackingRepository.trackingList

    /** Returns today's mood tracking entry */
    fun currentDay() = moodTrackingRepository.currentDayLocally()

    /** Provides list of mood emojis */
    fun getEmojisStatus(context: Context) = moodTrackingRepository.getEmojisStatus(context)

    /** Provides list of possible mood reasons */
    fun getEffectingMood(context: Context) = moodTrackingRepository.getEffectingMood(context)

    /** Stores selected emoji reference for use in UI flow */
    fun setEmoji(emoji: EmojiMood) { _emoji = emoji }
    /** Returns currently selected emoji */
    fun getEmoji() = _emoji
    /** Saves user pre-mood selection and reasons for current day */
    fun updateCurrentTaskPreMood(reasons: List<Int>?, extraReasons: String?) {
        val currentDay = currentDay()
        currentDay.status?.preChecked = true
        currentDay.status?.preMoodIndex = preMoodIndex
        currentDay.status?.reasons = reasons
        currentDay.status?.extraReasons = extraReasons

        viewModelScope.launch {
            moodTrackingRepository.dailyProgramRepository.updateCurrentDayLocally(currentDay)
            moodTrackingRepository.dailyProgramRepository.updateCurrentDayRemotely(currentDay)
        }
    }

    /** Saves user post-mood selection for current day */
    fun updateCurrentDayPostMood() {
        val currentDay = currentDay()
        currentDay.status?.postChecked = true
        currentDay.status?.postMoodIndex = postMoodIndex

        viewModelScope.launch {
            moodTrackingRepository.dailyProgramRepository.updateCurrentDayLocally(currentDay)
            moodTrackingRepository.dailyProgramRepository.updateCurrentDayRemotely(currentDay)
        }
    }

    /** Sets chosen pre-mood index */
    fun setPreMoodIndex(index: Int) {
        preMoodIndex = index
    }

    /** Sets chosen post-mood index */
    fun setPostMoodIndex(index: Int) {
        postMoodIndex = index
    }

    /** Pushes full daily mood tracking log to remote DB */
    fun storeDayMoodTrackingRemotely() {
        viewModelScope.launch {
            moodTrackingRepository.storeDayMoodTrackingRemotely()
        }
    }

    /** Requests next program day data from repository */
    fun getNextDay(day: Int) {
        log("getNextDay $day")
        viewModelScope.launch {
            moodTrackingRepository.dailyProgramRepository.getNextDay(day)
        }
    }

    /** Returns selected post-mood index */
    fun getPostMoodIndex(): Int = postMoodIndex

    /** Retrieves mood tracking history from remote server */
    fun retrieveTracingListRemotely(userId: String) {
        viewModelScope.launch {
            try {
                moodTrackingRepository.retrieveTracingListRemotely(userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
