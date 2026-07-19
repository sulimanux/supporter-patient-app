package com.app.sanad.auth.presentation

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.app.sanad.R
import com.app.sanad.auth.data.entity.UserProfile
import com.app.sanad.auth.data.repo.AuthRepo
import com.app.sanad.users.patient.dailyprogram.data.repo.DailyProgramRepository
import com.app.sanad.util.SUPPORTER
import com.app.sanad.util.IS_FIRST_TIME
import com.app.sanad.util.IS_USER_LOGGED
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.USER
import com.app.sanad.util.log
import javax.inject.Inject

/**
 * Purpose: Manages authentication operations and user state.
 * Handles login, signup, validation, supporter-invitation flow,
 * remote user fetching, local persistence, and user onboarding flags.
 */


@SuppressLint("StaticFieldLeak")
@HiltViewModel
class AuthViewModel @Inject constructor(
    val sharedPreferences: SharedPreferencesManager,
    private val dailyProgramRepo: DailyProgramRepository,
    private val authRepo: AuthRepo,
) : ViewModel() {

    var context:Context? = null
    private var partner: UserProfile? = null
    private var _authStatus = MutableLiveData<String?>()
    val authStatus: LiveData<String?> = _authStatus
    var intGender = MutableLiveData<Int?>()
    var strGender = MutableLiveData<String?>()
    var intAge = MutableLiveData<Int?>()
    var strAge = MutableLiveData<String?>()
    val currentLang = MutableLiveData<String>()
    var name = MutableLiveData<String?>()
    var invitationCode = MutableLiveData<String>()
    var email = MutableLiveData<String?>()
    var password = MutableLiveData<String?>()
    val typeOfUser = MutableLiveData<String?>()
    var errorMessage: String? = ""

    // Returns a UserProfile object based on the current input values
    private fun userProfile(): UserProfile {
        return UserProfile(
            name = name.value,
            email = email.value,
            password = password.value,
            gender = intGender.value,
            ageGroup = intAge.value,
            typeOfUser = typeOfUser.value ?: "",
        )
    }

    fun signUp( context: Context) {
        this.context = context
        try {
            viewModelScope.launch {
                if (userProfile().typeOfUser == SUPPORTER) {
                    isValidInvitation(invitationCode.value!!)
                } else {
                    userRegistration(userProfile())
                }
            }
        }
        catch (e: Exception) {
            log("${e.message}")
        }

    }

    // Checks if the supporter's invitation code is valid
    private suspend fun isValidInvitation(invitationCode: String) {
        val partner = authRepo.isValidInvitation(invitationCode)
        if (partner != null) {
            this.partner = partner // user  not supporter
            val userProfile = userProfile() // supporter
            userProfile.partnerId = partner.id
            userProfile.partnerEmail = partner.email
            supporterRegistration(userProfile)
        } else {
            _authStatus.value = context?.getString(R.string.user_not_found)
        }
    }

    // Registers the supporter if invitation is val
    private suspend fun supporterRegistration(userProfile: UserProfile) {
        val result = authRepo.signUp(userProfile)
        if (result.isNotEmpty()) { "g"
            _authStatus.value = result
        } else {
            addPartnerToSupporter(userProfile.id ?: "")
        }
    }

    // Links the user to supporter
    private suspend fun addPartnerToSupporter(userId: String) {
        val result = authRepo.linkPartnerToUser(userId, partner?.id ?: "")
        if (result.isNotEmpty()) {
            _authStatus.value = result // exc
        } else {
            addSupporterToUser(userId)
        }
    }

    // Links the supporter to the  user
    private suspend fun addSupporterToUser(partnerId: String) {
        // first arg => user id , second arg => supporter id
        val result = authRepo.linkPartnerToUser(partner?.id ?: "", partnerId)
        if (result.isNotEmpty()) {
            _authStatus.value = result
            log(result)
        } else {
            updateUserData()
        }
    }

    // Updates the  user data after linking
    private suspend fun updateUserData(){
        val updatedNumber = partner?.supportersNumber!!.plus(1)
        val updatedData: HashMap<String, Any> = hashMapOf(
            "supportersNumber" to updatedNumber,
            "hasPartner" to true,
            "invitationCode" to "%%%%%%%$$%%f%%%%%%lfjdgjkfdsgkfd;g%%",
            "invitationUsed" to true
        )
        val result = authRepo.updateUserData(partner?.id ?: "", updatedData)
        if (result.isNotEmpty()) {
            _authStatus.value = result
            log(result)
        } else {
            _authStatus.value = ""

        }
    }

    // Registers a regular user
    private suspend fun userRegistration(userProfile: UserProfile) {
        val result = authRepo.signUp(userProfile)
        if (result.isNotEmpty()) {
            _authStatus.value = result
            log(result)
        } else {
            fetchContentDailyProgramRemote(1)
        }
    }

    // Fetches daily program content after successful user registration
    private fun fetchContentDailyProgramRemote(numberOfDay: Int) {

        viewModelScope.launch {
            val result = dailyProgramRepo.fetchContentDailyProgram(numberOfDay)
            if (result) {
                _authStatus.value = ""
            } else {
                _authStatus.value = "error"
            }
        }
    }

    // Validates login form (email + password)
    fun validToLogin(context: Context): Boolean {
        log("validToLogin() called with: context = $context")
        if (!isEmailValid(context)) {
            return false
        } else if (!isPasswordValid(context)) {
            return false
        }
        return true
    }

    fun setAge(int: Int) {
        intAge.value = int
    }

    fun setStrAge(context: Context) {
        intAge.value?.let {
            when (intAge.value) {
                1 -> strAge.value = context.getString(R.string.young_adulthood1)
                2 -> strAge.value = context.getString(R.string.middle_age1)
                3 -> strAge.value = context.getString(R.string.older)
            }
        }
    }



    fun setFavoriteLange(lang: String) {
        currentLang.value = lang
    }

    fun setGender(int: Int) {
        intGender.value = int
    }
    fun setStrGender(context: Context ) {
        val gender = intGender.value
        gender?.let {
            when (gender) {
                1 -> strGender.value = context.getString(R.string.male)
                2 -> strGender.value = context.getString(R.string.female)
            }
        }
    }

    // Validates all registration fields before submitting
    fun validToRegisterUser(context: Context): Boolean {
        if (!isValidUserType(context)) return false
        if (!isValidName(context)) return false
        if (!isEmailValid(context)) return false
        if (!isPasswordValid(context)) return false
        if (!isValidAgeGroup(context)) return false
        if (!isValidGender(context)) return false
        if (typeOfUser.value == SUPPORTER) return isValidInvitationCode(context)
        return true
    }

    fun isEmailValid(context: Context): Boolean {
        log("isEmailValid() called")
        return if (email.value.isNullOrEmpty()) {
            log("isEmailValid() isNullOrEmpty()")
            errorMessage = context.getString(R.string.enter_email)
            false
        } else {
            log("isEmailValid() !isNullOrEmpty()")
            email.value = email.value!!.trim()
            log(email.value!!)
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            if (!email.value!!.matches(emailPattern.toRegex())) {
                errorMessage = context.getString(R.string.email_error)
                false
            } else true
        }
    }

    private fun isPasswordValid(context: Context): Boolean {
        log("isPasswordValid() called with: context = $context")
        return if (password.value.isNullOrEmpty()) {
            errorMessage = context?.getString(R.string.enter_password)
            false
        } else {
            if (password.value!!.trim().length < 6) {
                errorMessage = context?.getString(R.string.pass_error)
                false
            } else true
        }
    }

    private fun isValidUserType(context: Context): Boolean {
        return if (typeOfUser.value.isNullOrEmpty()) {
            errorMessage = context?.getString(R.string.select_user_type)
            false
        } else true
    }

    private fun isValidName(context: Context): Boolean {
        log("isValidName() called with: name = ${name.value}")
        return if (name.value.isNullOrEmpty()) {
            errorMessage = context?.getString(R.string.enter_username)
            false
        } else true
    }

    private fun isValidAgeGroup(context: Context): Boolean {
        return if (typeOfUser.value == SUPPORTER) {
            true
        } else {
            if (strAge.value == context?.getString(R.string.age_group)) {
                errorMessage = context?.getString(R.string.select_age_category)
                false
            } else true
        }
    }

    private fun isValidGender(context: Context): Boolean {
        return if (typeOfUser.value == SUPPORTER) {
            true
        } else {
            if (strGender.value == context?.getString(R.string.gender)) {
                errorMessage = context?.getString(R.string.select_gender)
                false
            } else {
                true
            }
        }
    }

    // Checks if invitation code is provided for supporters
    private fun isValidInvitationCode(context: Context): Boolean {
        return if (this.invitationCode.value.isNullOrEmpty()) {
            errorMessage = context?.getString(R.string.enter_invitation)
            false
        } else true
    }

    // Clears all user input data
    fun clearData() {
        name.value = null
        email.value = null
        password.value = null
        typeOfUser.value = null
        intAge.value = null
        intGender.value = null
    }
    fun currentUserProfile() = authRepo.currentUserProfile()



    fun updateAuthStatusLocale(isFirstTime: Boolean ) {

        currentUserProfile().typeOfUser
        sharedPreferences.storeBoolean(IS_FIRST_TIME,isFirstTime)
        sharedPreferences.storeBoolean(IS_USER_LOGGED,true)

    }

    fun login() {
        viewModelScope.launch {
            val result: String =
                authRepo.signInWithEmailAndPassword(email.value!!, password.value!!)
            if (result.isEmpty()) {
          retrieveUserRemote()
      }else{
          _authStatus.value = result
      }
        }
    }

    // Retrieves user data from remote after successful login
    private suspend fun retrieveUserRemote() {

        val (userProfile, message) = authRepo.retrieveUserRemoteByEmail(email.value!!)
        if (message.isNotEmpty()) {
            log("retrieveUserRemote() error ")
            _authStatus.value = "Error, please try again."
        } else {
            log("retrieveUserRemote() success ")
            storeUserLocally(userProfile!!)
        }

    }

    // Stores user data locally in device storage
    private fun storeUserLocally(userProfile: UserProfile) {
        try {
            val result = authRepo.storeUserDataLocally(userProfile)
            if (result) {
                log("storeUserLocally() success ")
                checkUsertype(userProfile)
            } else {
                log("storeUserLocally() error ")
                _authStatus.value = "Error, please try again."
            }
        } catch (e: Exception) {
            log("storeUserLocally() Exception ${e.message} ")
            _authStatus.value = "Error, please try again."
        }

    }

    // Checks the user type and proceeds accordingly
    private fun checkUsertype(userProfile: UserProfile) {
        log(userProfile.typeOfUser.toString())

        when (userProfile.typeOfUser) {
            SUPPORTER -> {
                log("SUPPORTER")
                _authStatus.value = ""
            }

            USER -> {
                log("USER")
                fetchContentDailyProgramRemote(userProfile.currentDay!!)
            }
        }

    }

    // Resets the auth status to null (used to clear error/success states)
    fun  resetAuthStatus() {
        _authStatus.value = null
    }

    // Tries to retrieve user by email and sends reset password email if found
    fun tryRetrieveUser(value: String , context: Context) {
        viewModelScope.launch {
            try {
                val (userProfile, message) = authRepo.retrieveUserRemoteByEmail(value)
                if (message.isEmpty()) {
                    sendResetEmail(userProfile!!.email!!)
                } else {
                    _authStatus.value = context.getString(R.string.user_not_found)
                }
            } catch (e: Exception) {
                _authStatus.value = context.getString(R.string.reset_password_failure)
            }
        }

    }

    // Sends password reset email to the given address
    private suspend fun sendResetEmail(email: String) {
        try {
            val result = authRepo.sendEmailToResetPassword(email)
            log("sendResetEmail() $result")
            _authStatus.value = result
        } catch (e: Exception) {
            _authStatus.value = e.message

        }


    }


}
