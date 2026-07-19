package com.app.sanad.users.patient.profile.data

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.USERS
import com.app.sanad.util.USER_PROFILE
import com.app.sanad.util.log

class ProfileRepo(
    private val firestore: FirebaseFirestore,
    private val fireStorage: FirebaseStorage,
    private val sharedPreferences: SharedPreferencesManager
) {

    // Retrieves the cached user profile stored locally in SharedPreferences
    fun userProfile() = sharedPreferences.getUserProfile()

    /**
     * Uploads a new profile image to Firebase Storage.
     * After the upload completes, retrieves the download URL,
     * updates the user's Firestore document with the new image URL,
     * and finally updates the local cached profile.
     *
     */
    suspend fun uploadImageToFireStorage(imageUri: Uri) {
        val storageRef = fireStorage.reference.child("users_images/${userProfile().id}")

        // Upload image file to Firebase Storage
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                // Once upload succeeds, get the download URL
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Update Firestore with the new image URL
                    firestore.collection(USERS)
                        .document(userProfile().id!!)
                        .update("imageUser", uri.toString())

                    // Update the locally stored profile
                    updateUserProfileLocally("imageUser", uri.toString())
                }
            }
            .await()  // Suspends coroutine until the upload task completes
    }

    /**
     * Updates a single key/value field of the user in Firestore,
     * then updates the same field locally in SharedPreferences.
     */
    suspend fun updateUserProfileRemotely(key: String, value: Any) {
        // Update the field on Firestore
        firestore.collection(USERS)
            .document(userProfile().id!!)
            .update(key, value)
            .await()

        // Update the field in local storage
        updateUserProfileLocally(key, value)
    }

    /**
     * Updates the locally stored user profile object.
     * Reads the current cached profile, modifies the field,
     * and then writes it back to SharedPreferences.
     */
    private fun updateUserProfileLocally(key: String, value: Any) {
        val userProfile = userProfile()
        userProfile.updateData(key, value)

        // Persist updated profile in SharedPreferences
        sharedPreferences.storeObject(USER_PROFILE, userProfile)

    }
}
