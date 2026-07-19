package com.app.sanad.users.patient.calender.data.repo

import android.content.Context
import com.app.sanad.R
import com.app.sanad.users.patient.calender.data.entity.CalenderActivity
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.log
import javax.inject.Inject
/**
 * Repository class for providing calendar activities tailored to the current user's profile.
 *
 * This class is responsible for:
 *  - Returning a predefined list of calendar activities.
 *  - Filtering activities based on the user's gender and religious preference.
 *
 * Activities are represented as [CalenderActivity] objects, each containing:
 *  - An image resource
 *  - A title (string resource)
 *  - A color code
 *  - Optional gender restriction (1 = male, 2 = female)
 *  - Optional religious restriction
 *
 * The repository uses [SharedPreferencesManager] to retrieve the current user's profile
 * and applies filters accordingly before returning the final list of activities.
 */
class CalenderActivitiesRepo @Inject constructor(

    private val sharedPreferences: SharedPreferencesManager

) {

    // Cached user profile retrieved from shared preferences
    val user = sharedPreferences.getUserProfile()

    /**
     * Returns a list of [CalenderActivity] objects filtered based on the current user's profile.
     *
     * @param context Context used to access string and drawable resources.
     * @return A filtered list of calendar activities suitable for the user.
     */
    fun getCalenderActivities(context: Context) : List<CalenderActivity>{

        val activities = listOf(

            CalenderActivity(
                R.drawable.calender_activity_1,
                context.getString(R.string.activity_1),
                "#4467c4",
                gender = 2
            ),
            CalenderActivity(
                R.drawable.calender_activity_2,
                context.getString(R.string.activity_2),
                "#4467c4",
                gender = 2
            ),
            CalenderActivity(
                R.drawable.calender_activity_3,
                context.getString(R.string.activity_3),
                "#4467c4",
                gender = 1
            ),
            CalenderActivity(
                R.drawable.calender_activity_4,
                context.getString(R.string.activity_4),
                "#4467c4"
            ),
            CalenderActivity(
                R.drawable.calender_activity_5,
                context.getString(R.string.activity_5),
                "#4467c4",
                gender = 1
            ),
            CalenderActivity(
                R.drawable.calender_activity_6,
                context.getString(R.string.activity_6),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_7,
                context.getString(R.string.activity_7),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_8,
                context.getString(R.string.activity_8),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_9,
                context.getString(R.string.activity_9),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_10,
                context.getString(R.string.activity_10),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_11,
                context.getString(R.string.activity_11),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_12,
                context.getString(R.string.activity_12),
                "#4467c4",
                gender = 1
            ),
            CalenderActivity(
                R.drawable.calender_activity_13,
                context.getString(R.string.activity_13),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_14,
                context.getString(R.string.activity_14),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_15,
                context.getString(R.string.activity_15),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_16,
                context.getString(R.string.activity_16),
                "#4467c4"
            ),
            CalenderActivity(
                R.drawable.calender_activity_17,
                context.getString(R.string.activity_17),
                "#4467c4",
                gender = 2
            ),
            CalenderActivity(
                R.drawable.calender_activity_18,
                context.getString(R.string.activity_18),
                "#4467c4",
                gender = 2
            ),
            CalenderActivity(
                R.drawable.calender_activity_19,
                context.getString(R.string.activity_19),
                "#4467c4",
                gender = 2
            ),
            CalenderActivity(
                R.drawable.calender_activity_20,
                context.getString(R.string.activity_20),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_21,
                context.getString(R.string.activity_21),
                "#4467c4",
                gender = 2
            ),
            CalenderActivity(
                R.drawable.calender_activity_22,
                context.getString(R.string.activity_22),
                "#4467c4",
                gender = 2
            ),
            CalenderActivity(
                R.drawable.calender_activity_23,
                context.getString(R.string.activity_23),
                "#4467c4",
                gender = 2
            ),
            CalenderActivity(
                R.drawable.calender_activity_24,
                context.getString(R.string.activity_24),
                "#4467c4",
                gender = 2
            ),
            CalenderActivity(
                R.drawable.calender_activity_25,
                context.getString(R.string.activity_25),
                "#4467c4",
                gender = 2
            ),
            CalenderActivity(
                R.drawable.calender_activity_26,
                context.getString(R.string.activity_26),
                "#4467c4",
                gender = 2
            ),
            CalenderActivity(
                R.drawable.calender_activity_27,
                context.getString(R.string.activity_27),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_28,
                context.getString(R.string.activity_28),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_29,
                context.getString(R.string.activity_29),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_30,
                context.getString(R.string.activity_30),
                "#4467c4",
                true
            ),
            CalenderActivity(
                R.drawable.calender_activity_31,
                context.getString(R.string.activity_31),
                "#4467c4",
                true
            ),
            CalenderActivity(
                R.drawable.calender_activity_32,
                context.getString(R.string.activity_32),
                "#4467c4",
                true
            ),

            CalenderActivity(
                R.drawable.calender_activity_13,
                context.getString(R.string.immerse_yourself),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_26,
                context.getString(R.string.invite_friends),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_26,
                context.getString(R.string.pair_up),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_8,
                context.getString(R.string.plan_an_outdoor),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_18,
                context.getString(R.string.offer_to_teach),
                "#4467c4",
            ),
            CalenderActivity(
                R.drawable.calender_activity_16,
                context.getString(R.string.organise_and_invite),
                "#4467c4",
            ),

            CalenderActivity(R.drawable.ic_plan_day, context.getString(R.string.special_activity) ,"#4467c4"),
        )

      return   filterBasedProfile(activities)
    }


    /**
     * Filters a list of activities according to the user's gender and religious preference.
     *
     * @param activities The list of activities to filter.
     * @return A filtered list of activities suitable for the user.
     */
    private fun filterBasedProfile(activities: List<CalenderActivity>): List<CalenderActivity>{
        val isReligious = user.religion!!
        val gender = user.gender!!
        log("$isReligious")
        var activities =
            filterBasedReligion(isReligious, activities)
        log("Size is ->  ${activities.size}")
        activities = filterBasedGender(activities, gender)
        return  activities
    }

    /**
     * Filters activities based on whether the user is religious.
     *
     * @param isReligious Boolean flag indicating if the user is religious.
     * @param activities List of activities to filter.
     * @return Filtered list respecting the user's religious preference.
     */
    private fun filterBasedReligion(
        isReligious: Boolean,
        activities: List<CalenderActivity>
    ) = if (isReligious) {
        activities
    } else {
        activities.filter { it.religion == false }
    }

    /**
     * Filters activities based on the user's gender.
     *
     * @param activities List of activities to filter.
     * @param gender User's gender (1 = male, 2 = female).
     * @return Filtered list respecting the user's gender.
     */

    private fun filterBasedGender(
        activities: List<CalenderActivity>,
        gender: Int
    ) = when (gender) {
        1 -> {
            activities.filter { it.gender == null || it.gender == 1 }
        }
        2 -> {
            activities.filter { it.gender == null || it.gender == 2 }
        }
        else -> {
            activities
        }
    }


}