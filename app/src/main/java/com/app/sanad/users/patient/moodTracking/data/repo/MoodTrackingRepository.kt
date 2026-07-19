package com.app.sanad.users.patient.moodTracking.data.repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.app.sanad.R
import com.app.sanad.users.patient.dailyprogram.data.repo.DailyProgramRepository
import com.app.sanad.users.patient.moodTracking.data.entity.DayMoodTracking
import com.app.sanad.users.patient.moodTracking.data.entity.EffectingMood
import com.app.sanad.users.patient.moodTracking.data.entity.EmojiMood
import com.app.sanad.users.patient.moodTracking.data.entity.SuggestionToDo
import com.app.sanad.util.USERS
import javax.inject.Inject

/**
 * Purpose: Provides mood-tracking data operations for the patient user.
 * Fetches, stores, and streams daily mood records from Firestore and local cache,
 * supplies emoji mood models, suggestions, and mood-influencing factors.
 */

class MoodTrackingRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    val dailyProgramRepository: DailyProgramRepository
){

var trackingList = MutableLiveData<List<DayMoodTracking>>()

     fun currentDayLocally() = dailyProgramRepository.getCurrentDayLocally()

    suspend fun  retrieveTracingListRemotely(userId: String) {
      val result =  firestore.collection(USERS).document(userId).collection("DayMoodTracking").get().await()
        trackingList.value =  result.toObjects(DayMoodTracking::class.java)
    }



    fun getEmojisStatus(context: Context) = listOf(
        EmojiMood(
            context.getString(R.string.happy),
            context.getString(R.string.it_s_great_to_see_you_happy),
            context.getString(R.string.let_us_channel_that_positive_energy),
            context.getString(R.string.here_are_some_suggestions_to_keep_your_mood_up),
            getSuggestionsForHappy(context),
            R.drawable.icon_mood, "#ffdb75", "#f8ca4a", "#fcf2d6"
        ),

        EmojiMood(
            context.getString(R.string.calm_relaxed),
            context.getString(R.string.there_s_nothing_like_the_feeling_of_peace),
            context.getString(R.string.let_s_hold_onto_this_tranquility),
            context.getString(R.string.here_are_some_suggestions_to_keep_your_mood_up),

            getSuggestionsForRelaxed(context),
            R.drawable.icon_mood3,
            "#ffb770", "#ff9e3e", "#fce9d5"
        ),

        EmojiMood(
            context.getString(R.string.tired_fatigued),
            context.getString(R.string.it_s_completely_okay),
            context.getString(R.string.let_s_take_small),
            context.getString(R.string.here_are_some_suggestions_to_improve_your_mood),

            getSuggestionsForTired(context),
            R.drawable.icon_mood6,
            "#61e5a3",
            "#26d57d", ""
        ),
        EmojiMood(
            context.getString(R.string.sad_down),
            context.getString(R.string.it_s_tough_feeling_down),
            context.getString(R.string.but_remember_even),
            context.getString(R.string.here_are_some_suggestions_to_improve_your_mood),

            getSuggestionsForSad(context),
            R.drawable.icon_mood1,
            "#d689d6",
            "#c051c0", "#f0d9f0"
        ),

        EmojiMood(
            context.getString(R.string.anxious_overwhelmed),
            context.getString(R.string.feeling_overwhelmed_is_hard),
            context.getString(R.string.but_you_re_stronger_than_you_think_let_s_focus_on_calming_your_mind),
            context.getString(R.string.here_are_some_suggestions_to_improve_your_mood),

            getSuggestionsForOverwhelmed(context),
            R.drawable.icon_mood5,
            "#87c1e7",
            "#5994d5", "#dbe7f4"
        ),

        EmojiMood(
            context.getString(R.string.angry_irritated),
            context.getString(R.string.anger_is_a_natural_emotion),
            context.getString(R.string.let_s_redirect_it_in_a_positive_and_constructive_way_to_create_change_and_growth),
            context.getString(R.string.here_are_some_suggestions_to_improve_your_mood),

            getSuggestionsForIrritated(context),
            R.drawable.icon_mood4,
            "#f59077",
            "#f0522b", "#fae2dc"
        ),
    )


    private fun getSuggestionsForHappy(context: Context) = listOf(
        SuggestionToDo(
            context.getString(R.string.share_your_happiness),
            context.getString(R.string.tip_use_the_app_s_daily_planne)
        ),
        SuggestionToDo(context.getString(R.string.start_a_creative_project), ""),
        SuggestionToDo(context.getString(R.string.use_your_energy), ""),
    )

    private fun getSuggestionsForRelaxed(context: Context) = listOf(
        SuggestionToDo(
            context.getString(R.string.enjoy_a_warm_cup),
            context.getString(R.string.tip_make_use_of_the_app)
        ),
        SuggestionToDo(
            context.getString(R.string.treat_yourself_to_a_self),
            context.getString(R.string.tip_make_self_care_a_priority)
        ),
        SuggestionToDo(
            context.getString(R.string.have_peaceful_evening),
            context.getString(R.string.tip_make_it_a_weekly)
        ),
    )

    private fun getSuggestionsForTired(context: Context) = listOf(
        SuggestionToDo(context.getString(R.string.close_your_eyes_and_take), ""),
        SuggestionToDo(context.getString(R.string.take_a_few_minutes), ""),
        SuggestionToDo(context.getString(R.string.take_the_time_to_prepare), ""),
    )

    private fun getSuggestionsForSad(context: Context) = listOf(
        SuggestionToDo(
            context.getString(R.string.write_down_three_things),
            context.getString(R.string.tip_make_use_of_the)
        ),
        SuggestionToDo(context.getString(R.string.call_or_message_someone_you_trust), ""),
        SuggestionToDo(context.getString(R.string.watch_a_funny_show_movie), ""),
    )

    private fun getSuggestionsForOverwhelmed (context: Context) = listOf(
        SuggestionToDo(
            context.getString(R.string.practice_deep),
            context.getString(R.string.tip_use_the_breathing_tool)
        ),
        SuggestionToDo(context.getString(R.string.take_a_mindful_slo), ""),
        SuggestionToDo(context.getString(R.string.organise_your_workstation), ""),

    )

    private fun getSuggestionsForIrritated (context: Context) = listOf(
        SuggestionToDo(context.getString(R.string.channel_your_anger_into), ""),
        SuggestionToDo(context.getString(R.string.grab_a_pen), ""),
        SuggestionToDo(
            context.getString(R.string.step_outside_and_take_deep_breaths_to_reset),
            context.getString(
                R.string.tip_use_the_breathi
            )
        ),
    )

    fun getEffectingMood(context: Context) = listOf(
        EffectingMood(context.getString(R.string.work), R.drawable.icon_work),
        EffectingMood(context.getString(R.string.family), R.drawable.icon_famely),
        EffectingMood(context.getString(R.string.friends), R.drawable.icon_frieds),
        EffectingMood(context.getString(R.string.health), R.drawable.icon_healtht),
        EffectingMood(context.getString(R.string.money), R.drawable.icon_money),
    )



    suspend fun storeDayMoodTrackingRemotely() {
        val currentDay = currentDayLocally()
        val userProfile = dailyProgramRepository.getUserProfile()
        val moodTracking = currentDay.toDayMoodTracking()
        try {
            firestore.collection(USERS)
                .document(userProfile.id!!)
                .collection("DayMoodTracking")
                .document(currentDay.status?.day!!.toString())
                .set(moodTracking).await()
        }catch (e:Exception){

        }
    }

    fun clear() {
        trackingList = MutableLiveData<List<DayMoodTracking>>()
    }


}