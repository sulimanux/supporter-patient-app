package com.app.sanad.users.supporter.partner.data

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.app.sanad.auth.data.entity.UserProfile
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.USERS
import javax.inject.Inject

class PartnerRepo @Inject constructor(
    private val firebase: FirebaseFirestore,
    private val sharedPreferences: SharedPreferencesManager,

) {
 val partner = MutableLiveData<UserProfile>()
 val user = sharedPreferences.getUserProfile()

    fun getPartnerLocally() =
        sharedPreferences.getPartnerProfile()

//
// suspend fun retrievePartner() {
//
//   val snapShot =  firebase.collection(USERS).document(user.partnerId!!).get().await()
//   partner.value = snapShot.toObject(UserProfile::class.java)
//
// }


}