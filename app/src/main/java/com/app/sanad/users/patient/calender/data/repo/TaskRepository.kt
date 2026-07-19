package com.app.sanad.users.patient.calender.data.repo

import com.app.sanad.users.patient.calender.data.daos.TaskDao
import com.app.sanad.users.patient.calender.data.entity.TaskEntity
import javax.inject.Inject

/**
 * Repository class for managing TaskEntity data in the application.
 *
 * This class serves as an intermediary between the [TaskDao] and the rest of the app,
 * providing a clean API for accessing, adding, updating, and deleting tasks.
 *
 * All methods are `suspend` functions to support asynchronous database operations
 * using Kotlin coroutines.
 *
 * @property taskDao The Data Access Object for performing database operations on TaskEntity.
 */
class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    /**
     * Adds a list of tasks to the database.
     * Existing tasks with the same primary key will be replaced.
     *
     * @param tasks The list of TaskEntity objects to add.
     * @return A list of row IDs corresponding to the inserted or replaced tasks.
     */
    suspend fun addTasks(tasks: List<TaskEntity>) = taskDao.addTasks(tasks)

    /**
     * Retrieves all tasks for a specific day.
     *
     * @param day The date for which to retrieve tasks.
     * @return A list of TaskEntity objects for the specified day.
     */
    suspend fun getTasks(day: String) = taskDao.getTasks(day)

    /**
     * Updates a task in the database. If the task does not exist, it will be added.
     *
     * @param task The TaskEntity to update or add.
     * @return The row ID of the updated or inserted task.
     */
    suspend fun updateTask(task: TaskEntity) = taskDao.addTask(task)

    /**
     * Deletes a task from the database by its ID.
     *
     * @param taskId The ID of the task to delete.
     */
    suspend fun deleteTask(taskId: Int) = taskDao.deleteTask(taskId)
}
