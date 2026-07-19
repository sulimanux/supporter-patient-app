package com.app.sanad.users.patient.supporters.data.repos

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.app.sanad.R
import com.app.sanad.auth.data.entity.UserProfile
import com.app.sanad.users.patient.supporters.data.entity.Instructions
import com.app.sanad.users.patient.supporters.data.entity.Partner
import com.app.sanad.util.LANGUAGE
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.USERS
import com.app.sanad.util.USER_PROFILE
import com.app.sanad.util.log

class SupportersRepo (
    private val firestore: FirebaseFirestore,
    val sharedPreferences: SharedPreferencesManager
) {

    val supportersProfile = MutableLiveData<List<UserProfile>?>()
    val partnerProfile = MutableLiveData<UserProfile?>()

    fun userProfile () = sharedPreferences.getUserProfile()
    fun currentLang() = sharedPreferences.getString(LANGUAGE)

    suspend fun retrievePartnersIds(userId: String) {
        try {
            val snapShot =
                firestore.collection(USERS).document(userId).collection("Partners").get().await()
            val partnersIds = mutableListOf<String>()
   // snapshot = [ "ddfd" , ]
            for (document in snapShot) {
                val partner = document.toObject(Partner::class.java)
                partnersIds.add(partner.id!!)
            }
             // partnersIds = ["ddfd"]

            retrievePartners(partnersIds)
        } catch (e: Exception) {
            supportersProfile.value = null
        }
    }

    private suspend fun retrievePartners(partnersIds: List<String>) {

        try {
            val supporters = mutableListOf<UserProfile>() //
            val snapshotQuery =
                firestore.collection(USERS).whereIn("id", partnersIds).get().await()
            for (document in snapshotQuery) {
                supporters.add(document.toObject(UserProfile::class.java))
            }
            supportersProfile.value = supporters
        } catch (e: Exception) {
            supportersProfile.value = null
        }

    }

    suspend fun storeNewInvitationCode(newInvitationCode: String): Void? {
        val updateData = hashMapOf<String, Any>(
                            "invitationCode" to newInvitationCode,
                            "invitationBase" to newInvitationCode,
                            "invitationUsed" to false,
                        )
        val userRef = firestore.collection(USERS).document(userProfile().id!!)
        return  userRef.update(updateData).await()
    }

    fun updateUserProfileLocal(newInvitationCode: String) {
        val userProfile = userProfile()
        userProfile.invitationCode = newInvitationCode
        userProfile.invitationBase = newInvitationCode
        userProfile.invitationUsed = false
        sharedPreferences.storeObject(USER_PROFILE, userProfile)
    }

   suspend fun updateSupporterPermissionsRemotely
               (supporterId: String, updatedPermissions:
           HashMap<String, Boolean>): Void? {
      return firestore.collection(USERS).
      document(supporterId)
          .update(updatedPermissions.toMap()).await()
    }

    suspend fun changeStatusOfSupporter(supporterId: String, updateData: Map<String, Int?>): Void? {

        return firestore.collection(USERS).document(supporterId).update(updateData).await()

    }

   suspend fun retrievePartner(id: String) {
       val result = firestore.collection(USERS).document(id).get().await()
       val supporter = result.toObject(UserProfile::class.java)
       partnerProfile.value = supporter
    }

    fun instructionsList():List <Instructions>{
        return listOf<Instructions>(
            Instructions(titleAr = "الخطوة ١: إنشاء رمز الدعوة للداعم" , titleEn = "Step 1:Create an invitation code for the supporter" , descAr = " اضغط على \"إضافة\" لنسخ الكود للمرة الأولى وأنشئ كودا جديدا لكل داعم يُضاف لاحقا.\n"
                    , descEn = "Click \"Add\" to copy the code for the first time and create a new code for each supporter added later."  , image = R.drawable.instru_step1),
            Instructions(titleAr = " الخطوة ٢: مشاركة الرمز مع الداعم" , titleEn = "Step 2: Share the code with the supporter" , descAr = " قم باختيار طريقتك المفضلة لمشاركة الرمز مع داعمك." , descEn = "Choose your preferred method to share the code with your supporter." , image = R.drawable.instru_step2),
            Instructions(titleAr = "الخطوة ٣: تحديد صلاحيات الداعم" , titleEn = "Step 3: Set supporter permissions" , descAr = " بعد إضافة الداعم، انتقل إلى قائمة الداعمين ثم اختر الداعم المطلوب وحدد صلاحياته من القائمة المتاحة." , descEn = "After adding the supporter, go to the Supporters list, then select the desired supporter and set their permissions from the available list." , image = R.drawable.instru_step3),
        )
    }

}