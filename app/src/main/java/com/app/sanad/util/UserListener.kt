package com.app.sanad.util

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.app.sanad.auth.data.entity.Partner
import com.app.sanad.auth.data.entity.UserProfile

object UserDataListener {


      var userListener: ListenerRegistration?  = null
    var partnerListener: ListenerRegistration?  = null

    fun addListener(sharedPre: SharedPreferencesManager ,  firestore: FirebaseFirestore){
        val userId = sharedPre.getUserProfile().id
        userListener =  firestore.collection(USERS).document(userId!!).addSnapshotListener { v , e ->
            val userProfile = v?.toObject<UserProfile>(UserProfile::class.java)
            log("UserData has Changed to => ${userProfile.toString()}")
            sharedPre.storeObject(USER_PROFILE , userProfile)
        }

    }

    fun addListenerForPartner(sharedPre: SharedPreferencesManager ,  firestore: FirebaseFirestore){
        val userId = sharedPre.getUserProfile().partnerId
        partnerListener =     firestore.collection(USERS).document(userId!!).addSnapshotListener { v , e ->
            val userProfile = v?.toObject<UserProfile>(UserProfile::class.java)
            val partner = Partner(id = userProfile?.id , name = userProfile?.name , currentDay = userProfile?.currentDay ,email = userProfile?.email , imageUser = userProfile?.imageUser)
           log("PARTNER_PROFILE has Changed to => $partner")
            sharedPre.storeObject(PARTNER_PROFILE , partner)
        }
    }

    fun removeListener() {
        userListener?.remove()
        partnerListener?.remove()
    }


}