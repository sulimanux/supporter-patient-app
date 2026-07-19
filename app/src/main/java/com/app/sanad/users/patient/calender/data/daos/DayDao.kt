package com.app.sanad.users.patient.calender.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.sanad.users.patient.calender.data.entity.DayEntity

/**
 * Data Access Object (DAO) for managing DayEntity objects in the local Room database.
 *
 * This interface defines the database operations related to the "days" table, including:
 *  - Adding a new day
 *  - Retrieving all days
 *  - Retrieving a specific day by date
 *
 * The methods are marked as `suspend` to support Kotlin coroutines for asynchronous
 * database operations, ensuring they run off the main thread.
 */
@Dao
interface DayDao {

    /**
     * Inserts a DayEntity into the "days" table.
     * If a conflict occurs (e.g., same primary key), the existing entry will be replaced.
     *
     * @param dayEntity The day entity to add.
     * @return The row ID of the newly inserted or replaced entity.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDay(dayEntity: DayEntity): Long

    /**
     * Retrieves all DayEntity records from the "days" table.
     *
     * @return A list of all stored DayEntity objects.
     */
    @Query("SELECT * FROM days")
    suspend fun getAllDays(): List<DayEntity>

    /**
     * Retrieves a single DayEntity by its date.
     *
     * @param date The date of the day to retrieve in string format.
     * @return The DayEntity matching the given date, or null if not found.
     */
    @Query("SELECT * FROM days WHERE day = :date")
    suspend fun getDay(date: String): DayEntity?
}
