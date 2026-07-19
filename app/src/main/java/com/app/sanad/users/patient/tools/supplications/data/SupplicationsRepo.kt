// Package for supplications data layer
package com.app.sanad.users.patient.tools.supplications.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.app.sanad.R
import com.app.sanad.posts.data.entity.Post
import com.app.sanad.posts.data.entity.Posts
import com.app.sanad.model.Supplication
import com.app.sanad.util.POSTS
import com.app.sanad.util.SUPPLICATIONS
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.USERS
import com.app.sanad.util.log

/**
 * Repository responsible for all supplication-related data operations.
 * Handles Firebase Firestore access and local shared preferences.
 */
class SupplicationsRepo(

    // Firestore instance for remote data operations
    val firestore: FirebaseFirestore,

    // Shared preferences manager for local user data
    val sharedPreferences: SharedPreferencesManager,

    ) {

    /**
     * Returns currently logged-in user
     */
    fun getUser() = sharedPreferences.getUserProfile()

    /**
     * Stores a new user supplication in Firestore
     */
    suspend fun storeUserSupplication(newSupplication: Supplication) {
        firestore.collection(USERS)
            .document(getUser().id!!)
            .collection(SUPPLICATIONS)
            .add(newSupplication)
            .await()

        log("User supplication stored successfully")
    }

    /**
     * Listens for real-time updates of user's supplications
     */
    fun listenToUserSupplications(onChange: (List<Supplication>) -> Unit) {
        firestore.collection(USERS)
            .document(getUser().id!!)
            .collection(SUPPLICATIONS)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    onChange(emptyList())
                    return@addSnapshotListener
                }
                val supplications = snapshots?.documents
                    ?.mapNotNull { doc ->
                        doc.toObject(Supplication::class.java)?.apply { id = doc.id }
                    }
                    ?: emptyList()
                onChange(supplications)
            }
    }

    /**
     * Listens for real-time updates of app-provided supplications
     */
    fun listenToAppSupplications(onChange: (List<Supplication>) -> Unit) {
        firestore.collection(SUPPLICATIONS)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    log("App supplications listen failed: ${error.message}")
                    onChange(emptyList())
                    return@addSnapshotListener
                }
                val supplications = snapshots?.documents
                    ?.mapNotNull { doc ->
                        doc.toObject(Supplication::class.java)?.apply { id = doc.id }
                    }
                    ?: emptyList()
                log("App supplications updated: ${supplications.size} items")
                onChange(supplications)
            }
    }

    /**
     * Updates an existing user supplication in Firestore
     */
    suspend fun updateUserSupplication(supplication: Supplication) {
        firestore.collection(USERS)
            .document(getUser().id!!)
            .collection(SUPPLICATIONS)
            .document(supplication.id!!)
            .set(supplication)
            .await()
        log("User supplication updated successfully")
    }

    /**
     * Deletes a user supplication from Firestore
     */
    suspend fun deleteUserSupplication(supplicationId: String) {
        firestore.collection(USERS)
            .document(getUser().id!!)
            .collection(SUPPLICATIONS)
            .document(supplicationId)
            .delete()
            .await()
        log("User supplication deleted successfully")
    }

    /**
     * Shares supplication content by adding a post to Firestore
     */
    suspend fun shareContent(post: Post): Boolean {
        return try {
            val result = firestore
                .collection(POSTS)
                .document(getUser().email!!)
                .get()
                .await()

            val posts: MutableList<Post> =
                if (result.exists()) {
                    result.toObject(Posts::class.java)?.posts ?: mutableListOf()
                } else {
                    mutableListOf()
                }

            posts.add(post)

            firestore
                .collection(POSTS)
                .document(getUser().email!!)
                .set(Posts(posts))
                .await()

            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Returns list of hand images used during counting
     */
    fun handsList() = getListHands()

    /**
     * Returns list of sebha images used during counting
     */
    fun sebhaList() = getListSebha()

    /**
     * Hand images sequence
     */
    fun getListHands() = listOf(
        R.drawable.image_hand1,   // 0
        R.drawable.image_hand2,   // 1
        R.drawable.image_hand3,
        R.drawable.image_hand4,
        R.drawable.image_hand5,
        R.drawable.image_hand6,
        R.drawable.image_hand7,
        R.drawable.image_hand8,
        R.drawable.image_hand9,
        R.drawable.image_hand10,
        R.drawable.image_hand11,
        R.drawable.image_hand12,
        R.drawable.image_hand13,
        R.drawable.image_hand14,
        R.drawable.image_hand15,
        R.drawable.image_hand16,
    )

    /**
     *
     * Sebha images sequence
     *
     */

    fun getListSebha() = listOf(
        R.drawable.image_sebha0,
        R.drawable.image_sebha1,
        R.drawable.image_sebha2,
        R.drawable.image_sebha3,
        R.drawable.image_sebha4,
        R.drawable.image_sebha5,
        R.drawable.image_sebha6,
        R.drawable.image_sebha7,
        R.drawable.image_sebha8,
        R.drawable.image_sebha9,
        R.drawable.image_sebha10,
        R.drawable.image_sebha11,
        R.drawable.image_sebha12,
        R.drawable.image_sebha13,
        R.drawable.image_sebha14,
        R.drawable.image_sebha16,
        R.drawable.image_sebha17,
        R.drawable.image_sebha18,
        R.drawable.image_sebha19,
        R.drawable.image_sebha20,
        R.drawable.image_sebha21,
        R.drawable.image_sebha22,
    )


}


