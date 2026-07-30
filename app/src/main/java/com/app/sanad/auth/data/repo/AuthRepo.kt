package com.app.sanad.auth.data.repo

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import com.app.sanad.auth.data.entity.UserProfile
import com.app.sanad.util.*

class AuthRepo(
    private val firestore: FirebaseFirestore,
    private val fireAuth: FirebaseAuth,
    private val firebaseMessaging: FirebaseMessaging,
    private val sharedPreferences: SharedPreferencesManager,
//    private val fireAnalytics: FirebaseAnalytics

) {

    // Registers a new user and stores profile data remotely and locally
    suspend fun signUp(userProfile: UserProfile): String {
        return try {
            val (authResult, errorOrId) =
                createUserWithEmailAndPassword(userProfile.email!!, userProfile.password!!)
            if (authResult) {
                val newUserProfile = newUserProfile(userProfile, errorOrId)
                storeUserDataRemote(newUserProfile)
                storeUserDataLocally(newUserProfile)
                ""
            } else {
                errorOrId
            }
        } catch (e: Exception) {
            e.message.toString()
        }
    }



    // Creates a user in Firebase Auth with email & password
    private suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): Pair<Boolean, String> {
        return try {
            val result = fireAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid
            if (uid != null) {
                Pair(true, uid)
            } else {
                Pair(false, "Registration failed: UID is null")
            }
        } catch (e: Exception) {

            Pair(false, e.message ?: "Registration error")
        }
    }

    // Creates a complete UserProfile with initial values based on user type
    private suspend fun newUserProfile(
        userProfile: UserProfile,
        userId: String
    ): UserProfile {
        userProfile.imageUser =
            if (userProfile.gender == 2)
                "https://firebasestorage.googleapis.com/v0/b/myproject-18932.appspot.com/o/images%2Fusers%20(2).png?alt=media&token=889b15ae-fd46-4255-a757-de712e1b5113"
            else
                "https://firebasestorage.googleapis.com/v0/b/myproject-18932.appspot.com/o/images%2Fman.png?alt=media&token=442a95f6-c82c-4725-9432-5fef0b516b06"


        userProfile.password = "***********"
        userProfile.id = userId
        userProfile.token = getToken()

        if (userProfile.typeOfUser == SUPPORTER) {
            userProfile.hasPartner = true
            userProfile.status = 1
            userProfile.religion = true
            userProfile.allowDailyProgramDetails = true
            userProfile.allowMoodTrackingDetails = true
            userProfile.allowPrivateMessages = true
        } else {
            val invitationCode = userId.take(8)
            // replacement when care connect with the user like &&&&&&&&&&&&&&&&&&
            userProfile.invitationCode = invitationCode
            // this for user ui  it will replacement later when user create a new code
            userProfile.invitationBase = invitationCode
            userProfile.invitationUsed = false
            userProfile.supportersNumber = 0
            userProfile.currentDay = 1
            userProfile.hasPartner = false
            userProfile.religion = true
        }

        return userProfile
    }

    // Saves user profile to Firestore
    private suspend fun storeUserDataRemote(newUserProfile: UserProfile): Boolean {
        return try {
            firestore
                .collection(USERS)
                .document(newUserProfile.id!!)
                .set(newUserProfile).await()

            true
        } catch (e: Exception) {
            false
        }
    }

    // Retrieves FCM token for the user
    private suspend fun getToken(): String {
        return try {
            firebaseMessaging.token.await()
        } catch (e: Exception) {
            ""
        }
    }

    // Saves user profile in local shared preferences
    fun storeUserDataLocally(userProfile: UserProfile): Boolean {
        log("storeUserDataLocally() called with: userProfile = $userProfile")


        return try {
            sharedPreferences.storeObject(USER_PROFILE, userProfile)
            true
        } catch (e: Exception) {
            false
        }
    }

    // Gets the current user profile from local storage
    fun currentUserProfile() =
        sharedPreferences.getUserProfile()

    // Validates supporter invitation code and returns the associated user
    suspend fun isValidInvitation(invitationCode: String): UserProfile? {
        return try {
            val snapshot = firestore.collection(USERS)
                .whereEqualTo(INVITATION_CODE, invitationCode).get().await()
            snapshot.documents.firstOrNull()?.toObject(UserProfile::class.java)

        } catch (e: Exception) {
            null
        }
    }

    // Creates a link between two users (supporter <-> partner)
    suspend fun linkPartnerToUser(userId: String, partnerId: String): String {
        return try {
            val data = hashMapOf("id" to partnerId) // done
            firestore.collection(USERS) // main col
                .document(userId)
                .collection(PARTNERS) // subCol
                .document(partnerId)
                .set(data)
                .await()
            ""
        } catch (e: Exception) {
            e.message.toString()
        }
    }

    // Updates a user's data in Firestore
    suspend fun updateUserData(userId: String, updatedData: HashMap<String, Any>): String {
        log("updateSupportersNumber $updatedData ")
        return try {
            firestore.collection(USERS)
                .document(userId)
                .update(updatedData)
                .await()
            ""
        } catch (e: Exception) {
            e.message.toString()
        }
    }

    // Logs in user using Firebase Auth
    suspend fun signInWithEmailAndPassword(email: String, password: String): String {
        return try {
            fireAuth.signInWithEmailAndPassword(email, password).await()
            log("login success2")
            ""
        } catch (e: Exception) {
            log("login failed2")
            e.toString()
        }
    }


    // Retrieves user profile from Firestore by email
    suspend fun retrieveUserRemoteByEmail(email: String): Pair<UserProfile?, String> {
        return try {
            val snapShot = firestore.collection(USERS)
                .whereEqualTo("email", email).get().await()
            val userProfile = snapShot.first()?.toObject(UserProfile::class.java)

            Pair(userProfile, "")

        } catch (e: Exception) {
            Pair(null, e.message ?: "Error retrieving user")
        }
    }

    // Sends password reset email via Firebase Auth
    suspend fun sendEmailToResetPassword(email: String): String {
        return try {
            fireAuth.sendPasswordResetEmail(email).await()
            ""
        } catch (e: Exception) {
            e.message ?: "There is an exception"
        }
    }
}
