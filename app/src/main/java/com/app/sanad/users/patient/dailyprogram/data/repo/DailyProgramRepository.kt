package com.app.sanad.users.patient.dailyprogram.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import com.app.sanad.users.patient.dailyprogram.data.entity.StatusDailyProgram
import com.app.sanad.users.patient.dailyprogram.data.daos.DayTaskDao
import com.app.sanad.users.patient.dailyprogram.data.entity.CurrentDay
import com.app.sanad.users.patient.dailyprogram.data.entity.DayTaskEntity
import com.app.sanad.util.CURRENT_DAY
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.USERS
import com.app.sanad.util.USER_PROFILE
import com.app.sanad.util.log
import javax.inject.Inject

/**
 * Repository responsible for fetching, storing, and updating
 * the daily program content both locally (Room/SharedPreferences)
 * and remotely (Firestore).
 */
class DailyProgramRepository @Inject constructor(
    private val dao: DayTaskDao,
    private val firestore: FirebaseFirestore,
    private val sharedPreferences: SharedPreferencesManager
) {


    /** Returns the user profile stored locally */
    fun getUserProfile() = sharedPreferences.getUserProfile()

    /**
     * Fetches daily program content for a specific day.
     * Loads from Room if available; otherwise fetches from Firestore and stores locally.
     */
    suspend fun fetchContentDailyProgram(numberOfDay: Int): Boolean {
        log("fetchContentDailyProgram() called with: numberOfDay = $numberOfDay")
        return try {
            val tasks = dao.getAllDayTasks()
            if (tasks?.isEmpty() == true) {
                log("fetchContentDailyProgram() tasks?.isEmpty() == true")
                val data = fetchContentDailyProgramRemote()
                storeDailyProgramListLocally(data)
            }
            log("fetchContentDailyProgram() after execute")
            getNextDay(numberOfDay) // prepares the CurrentDay object
            true
        } catch (e: Exception) {
            log(e.message.toString())
            false
        }
    }

    /** Updates the current day field in the remote user profile in Firestore */
    private suspend fun updateNumberDayInUseProfile(numberOfDay: Int) {
        val userProfile = sharedPreferences.getUserProfile()
        val userId = userProfile.id!!
        val data: HashMap<String, Any> = hashMapOf("currentDay" to numberOfDay)
        try {
            firestore.collection(USERS).document(userId).update(data).await()
        } catch (e: Exception) {
            log(e.message.toString())
        }
    }

    /** Fetches the full list of daily program content from Firestore */
    private suspend fun fetchContentDailyProgramRemote(): List<DayTaskEntity> {
        val dailyProgramList = mutableListOf<DayTaskEntity>()
        val querySnapShot = firestore.collection("daily_programs").get().await()
        for (document in querySnapShot) {
            val dayTaskEntity = document.toObject(DayTaskEntity::class.java)
            dailyProgramList.add(dayTaskEntity)
        }
        return dailyProgramList
    }


    /** Stores a list of DayTaskEntity objects into Room database */
    private suspend fun storeDailyProgramListLocally(data: List<DayTaskEntity>) {
        log("Storing start ...................")
        dao.insertAll(data)
        log("Storing completed ...................")
    }

    /**
     * Filters out spiritual tasks if user has no religion
     * and prepares the CurrentDay object with proper status.
     */
    private fun filterBasedProfile(dayTask: DayTaskEntity, day: Int): CurrentDay {
        val statusDailyProgram = StatusDailyProgram(day = day)
        val userProfile = sharedPreferences.getUserProfile()
        val isReligious = userProfile.religion!!

        if (!isReligious) {
            dayTask.spiritual = null
            statusDailyProgram.remaining = 2
            dayTask.behaviorActivation = dayTask.behaviorActivation?.filter { it?.religion == false }
        }

        return CurrentDay(userProfile.email, dayTask, statusDailyProgram)
    }

    /** Retrieves a specific day’s task from Room */
    private suspend fun getDayTaskFromRoom(day: Int): DayTaskEntity {
        return dao.getDayTaskById(day)!!
    }

    /** Retrieves the current day object stored locally as JSON in SharedPreferences */
    fun getCurrentDayLocally(): CurrentDay {
        val string = sharedPreferences.getString(CURRENT_DAY, null.toString())
        val gson = Gson()
        return gson.fromJson(string, CurrentDay::class.java)
    }

    /** Stores the CurrentDay object locally in SharedPreferences */
    fun updateCurrentDayLocally(currentDay: CurrentDay) {
        sharedPreferences.storeObject(CURRENT_DAY, currentDay)
    }

    /** Updates the current day remotely in Firestore */
    suspend fun updateCurrentDayRemotely(currentDay: CurrentDay) {
        try {
            val userProfile = sharedPreferences.getUserProfile()
            firestore.collection(USERS)
                .document(userProfile.id!!)
                .collection("DailyProgram")
                .document(userProfile.id!!)
                .set(currentDay)
                .await()
        } catch (e: Exception) {
            log(e.message.toString())
        }
    }

    /**
     * Retrieves the task for a specific day,
     * filters it based on user profile, and updates both local and remote storage.
     */
    suspend fun getNextDay(day: Int) {
        try {
            log("getNextDay Re $day")
            log("tas;s -> ${dao.getAllDayTasks()}")
            val dayTask = dao.getDayTaskById(day)
            log("getNextDay Re dart  $dayTask")
            val currentDay = filterBasedProfile(dayTask!!, day)
            updateUserProfile(day)
            updateCurrentDayLocally(currentDay)
            updateNumberDayInUseProfile(day)
            updateCurrentDayRemotely(currentDay)
        }catch (e:Exception){

        }

    }

    private fun updateUserProfile(day: Int){
        val user  = sharedPreferences.getUserProfile()
        user.currentDay = day
        sharedPreferences.storeObject(USER_PROFILE, user)
    }
}