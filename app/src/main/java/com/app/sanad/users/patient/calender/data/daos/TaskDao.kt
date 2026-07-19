package com.app.sanad.users.patient.calender.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.sanad.users.patient.calender.data.entity.TaskEntity

/**
 * Data Access Object (DAO) for managing TaskEntity objects in the local Room database.
 *
 * This interface defines the database operations related to the "tasks" table, including:
 *  - Adding a single task or a list of tasks
 *  - Retrieving tasks for a specific day
 *  - Deleting a task by its ID
 *
 * The methods are marked as `suspend` where appropriate to support Kotlin coroutines
 * for asynchronous database operations, keeping them off the main thread.
 * Insert operations return the row ID(s) of the newly inserted or replaced entity/entities.
 */
@Dao
interface TaskDao {

    /**
     * Inserts a list of TaskEntity objects into the "tasks" table.
     * If a conflict occurs (e.g., same primary key), existing entries will be replaced.
     *
     * @param tasks The list of task entities to add.
     * @return A list of row IDs corresponding to the inserted or replaced tasks.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTasks(tasks: List<TaskEntity>): List<Long>

    /**
     * Inserts a single TaskEntity into the "tasks" table.
     * If a conflict occurs, the existing entry will be replaced.
     *
     * @param task The task entity to add.
     * @return The row ID of the newly inserted or replaced task.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTask(task: TaskEntity): Long

    /**
     * Retrieves all tasks for a specific day.
     *
     * @param day The date for which to retrieve tasks.
     * @return A list of TaskEntity objects for the specified day.
     */
    @Query("SELECT * FROM tasks WHERE day = :day")
    suspend fun getTasks(day: String): List<TaskEntity>

    /**
     * Deletes a task from the "tasks" table by its ID.
     *
     * @param taskId The ID of the task to delete.
     */
    @Query("DELETE FROM tasks WHERE taskId = :taskId")
    suspend fun deleteTask(taskId: Int)
}
