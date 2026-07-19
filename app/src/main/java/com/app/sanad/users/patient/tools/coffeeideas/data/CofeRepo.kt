// Package declaration: defines the namespace of this repository
package com.app.sanad.users.patient.tools.coffeeideas.data

// Imports: required libraries for LiveData, Firebase, coroutines, and project utilities
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.app.sanad.auth.data.entity.UserProfile
import com.app.sanad.notifications.data.entities.Notification
import com.app.sanad.notifications.data.entities.NotificationsEnum
import com.app.sanad.notifications.data.repos.NotificationsRepo
import com.app.sanad.users.supporter.tools.cofe.data.entity.UserIdea
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.USERS
import com.app.sanad.util.USER_IDEAS
import com.app.sanad.util.log
import javax.inject.Inject

// Repository class for managing coffee ideas and related Firebase operations
class CofeRepo @Inject constructor(
    val sharedPreferences: SharedPreferencesManager, // SharedPreferences manager for local user data
    val firestore: FirebaseFirestore,
    val notificationsRepo: NotificationsRepo
) {

    // Retrieve the current logged-in user's profile from SharedPreferences
    val user = sharedPreferences.getUserProfile()

    // LiveData to hold the currently tracked user idea for UI observation
    val userIdea = MutableLiveData<UserIdea>()

    /**
     * Share a coffee idea with the user's supporter/partner.
     * Stores the idea in the current user's document and updates it for the partner as well.
     */
    suspend fun shareIdeaWithSupporter(idea: UserIdea, partner: UserProfile) {
        // Save the idea in the current user's ideas collection
        firestore.collection(USERS)
            .document(user.id!!) // User ID cannot be null
            .collection(USER_IDEAS)
            .document(user.id!!)
            .set(idea)
            .await() // Wait for operation to complete

        // Update the partner's document with the same idea
        updateIdeaAtPartner(idea, partner)
    }

    /**
     * Update the same idea in the partner's collection
     */
    suspend fun updateIdeaAtPartner(idea: UserIdea, partner: UserProfile) {
        firestore.collection(USERS)
            .document(partner.id!!)
            .collection(USER_IDEAS)
            .document(partner.id!!)
            .set(idea)
            .await()

      // sendNotification
        val notification = Notification(
            bodyAr = "لديك رسالة جديدة من صديقك فى مقهى الافكار",
            bodyEn = "You have a new message from your friend in Coffee Ideas",
            type = NotificationsEnum.Coffee.toString()
        )
        notificationsRepo.sendNotification(partner.id!! , notification)
    }

    /**
     * Send a response from the supporter back to the user.
     * Saves the updated idea in the partner's document and deletes it from the current user's document.
     */
    suspend fun sendSupporterResponse(idea: UserIdea) {
        firestore.collection(USERS)
            .document(user.partnerId!!) // Partner ID must exist
            .collection(USER_IDEAS)
            .document(user.partnerId!!)
            .set(idea)
            .await()

        // Remove the idea from the user's collection after sending response
        firestore.collection(USERS)
            .document(user.id!!)
            .collection(USER_IDEAS)
            .document(user.id!!)
            .delete()
    }

    /**
     * Listen for changes in the current user's idea document.
     * Updates the LiveData whenever there is a change in Firestore.
     */
    suspend fun listenToIdeaChanges() {
        firestore.collection(USERS)
            .document(user.id!!)
            .collection(USER_IDEAS)
            .document(user.id!!)
            .addSnapshotListener { value, error ->
                // Log any errors from the snapshot listener
                log("error is $error")
                // Convert Firestore document to UserIdea object and post to LiveData
                userIdea.value = value?.toObject<UserIdea>(UserIdea::class.java)
            }

    }


    /**
     * Mark a specific field/key in the idea document as "seen" by the user.
     */



    suspend fun updateSeenByUser(key: String) {
        firestore.collection(USERS)
            .document(user.id!!)
            .collection(USER_IDEAS)
            .document(user.id!!)
            .update(key, true) // Set the field value to true
            .await()
    }



}
