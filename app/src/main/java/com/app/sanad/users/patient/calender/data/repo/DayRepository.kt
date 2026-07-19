package com.app.sanad.users.patient.calender.data.repo

import com.app.sanad.users.patient.calender.data.daos.DayDao
import com.app.sanad.users.patient.calender.data.entity.DayEntity
import javax.inject.Inject

/**
 * Repository class for managing DayEntity data in the application.
 *
 * This class acts as a mediator between the [DayDao] and the rest of the app, providing
 * a clean API for accessing and modifying day-related data.
 *
 * Responsibilities:
 *  - Add a new day to the database.
 *  - Retrieve all stored days.
 *  - Retrieve a specific day by its date.
 *
 * All methods are `suspend` functions to support asynchronous database operations
 * using Kotlin coroutines.
 *
 * @property dayDao The Data Access Object for performing database operations on DayEntity.
 */
class DayRepository @Inject constructor(val dayDao: DayDao) {

    /**
     * Adds a new day to the database.
     *
     * @param dayEntity The day entity to add.
     * @return The row ID of the newly inserted or replaced day.
     */
    suspend fun addDay(dayEntity: DayEntity) = dayDao.addDay(dayEntity)

    /**
     * Retrieves all days from the database.
     *
     * @return A list of all stored DayEntity objects.
     */
    suspend fun getAllDays() = dayDao.getAllDays()

    /**
     * Retrieves a specific day by its date.
     *
     * @param date The date of the day to retrieve as a String.
     * @return The DayEntity matching the given date, or null if not found.
     */
    suspend fun getDay(date: String) = dayDao.getDay(date)
}
