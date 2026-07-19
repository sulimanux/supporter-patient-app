package com.app.sanad.users.patient.tools.gratitude.data.repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.app.sanad.R
import com.app.sanad.users.patient.tools.gratitude.data.entity.Gratitude
import com.app.sanad.util.GRATITUDE
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.USERS

class GratitudeRepo(
    private val sharedPreferences: SharedPreferencesManager,
    private val fireStore: FirebaseFirestore
) {

    // Returns the currently logged-in user profile
    fun user() = sharedPreferences.getUserProfile()

    // LiveData holding the list of gratitude entries
    val gratitudeList = MutableLiveData<List<Gratitude>>()

    /**
     * Returns a static list of gratitude questions from string resources
     */
    fun getGratitudeQuestionsList(context: Context): List<String> {
        return listOf(
            context.getString(
                R.string.have_you_had_the_opportunity_to_help_someone_new_how_do_you_feel_about_that
            ),
            context.getString(
                R.string.what_is_the_good_thing_that_happened_to_you_this_week
            ),
            context.getString(
                R.string.what_is_the_nice_thing_someone_did_for_you_recently
            ),
            context.getString(
                R.string.who_is_the_person_who_is_always_with_you_and_how_do_you_feel_about_them
            )
        )
    }

    /**
     * Saves a gratitude entry to Firestore under the current user
     */
    suspend fun saveGratitudeRemotely(gratitude: Gratitude) {
        fireStore.collection(USERS)
            .document(user().id!!)
            .collection(GRATITUDE)
            .add(gratitude)
            .await()
    }

    /**
     * Listens for gratitude list updates from Firestore
     * and updates LiveData in real time
     */
    fun retrieveGratitudeListRemotely() {
        fireStore.collection(USERS)
            .document(user().id!!)
            .collection(GRATITUDE)
            .addSnapshotListener { value, error ->

                // Convert Firestore documents to Gratitude objects
                val data = value?.toObjects(Gratitude::class.java) ?: emptyList()
                gratitudeList.value = data
            }
    }
}