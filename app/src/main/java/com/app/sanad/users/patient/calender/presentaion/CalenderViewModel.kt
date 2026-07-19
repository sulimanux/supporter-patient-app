package com.app.sanad.users.patient.calender.presentaion

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.app.sanad.users.patient.calender.data.entity.CalenderActivity
import com.app.sanad.users.patient.calender.data.entity.DayEntity
import com.app.sanad.users.patient.calender.data.entity.TaskEntity
import com.app.sanad.users.patient.calender.data.repo.CalenderActivitiesRepo
import com.app.sanad.users.patient.calender.data.repo.DayRepository
import com.app.sanad.users.patient.calender.data.repo.TaskRepository
import com.app.sanad.util.log
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel class for managing calendar-related data and interactions in the UI.
 *
 * Responsibilities:
 *  - Provide a list of calendar activities based on the user's profile.
 *  - Manage selected activities and custom activities.
 *  - Maintain LiveData objects for days and tasks to update the UI reactively.
 *
 * Properties:
 *  - [pickedDate]: The currently selected calendar day.
 *  - [customActivity]: A custom activity created by the user.
 *  - [chosenActivities]: List of activities chosen for the picked date.
 *  - [daysList]: LiveData containing all days with data in the calendar.
 *  - [taskList]: LiveData containing tasks for the selected day.
 *
 * All database or use case operations are performed asynchronously using Kotlin coroutines
 * on the IO dispatcher.
 *
 * calendar days and tasks.
 */
@HiltViewModel
class CalenderViewModel @Inject constructor(
    private val calenderRepo: CalenderActivitiesRepo,
    private val dayRepository: DayRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {


    private lateinit var pickedDate: CalendarDay
    private lateinit var customActivity: CalenderActivity
    val today = CalendarDay.today()
    private lateinit var chosenActivities: List<CalenderActivity>

    private val _daysList = MutableLiveData<HashSet<CalendarDay>>()
    val daysList: LiveData<HashSet<CalendarDay>> get() = _daysList
    private val _taskList = MutableLiveData<List<TaskEntity>>()
    val taskList: LiveData<List<TaskEntity>> get() = _taskList

    /**
     * Returns the list of calendar activities filtered based on the user's profile.
     */
    fun getCalenderActivities(context: Context): List<CalenderActivity> {
        return calenderRepo.getCalenderActivities(context)

    }


    /**
     * Setters and getters for picked date, custom activity, and chosen activities.
     */
    fun setCustomActivity(activity: CalenderActivity) = run { customActivity = activity }

    fun setChosenActivities(activities: List<CalenderActivity>) =
        run { chosenActivities = activities }

    fun getChosenActivities() = chosenActivities

    fun setPickedDate(date: CalendarDay) = run { pickedDate = date }
    fun getPickedDate() = pickedDate
    fun getDayEntity() = DayEntity(day = pickedDate.calendar.time.toString())


    /**
     * Creates a day plan with tasks for the picked date.
     * Checks if the day already exists; creates it if not, then adds tasks.
     */
    fun createDayPlan(day: DayEntity, tasks: List<TaskEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = dayRepository.getDay(day.day)
                    if (result != null) {
                        addTasks(tasks)
                    } else {
                        val result = dayRepository.addDay(day)
                        if (result != null) {
                            val id = result
                            log("Successfully created day with ID: $id")
                            addTasks(tasks)
                        } else {
                            log("Failed to create day")
                        }
                    }
            } catch (e: Exception) {
                log("Unexpected error: ${e.message}")
            }
        }
    }

    /**
     * Fetches all stored days and posts them to [daysList] LiveData.
     */
    fun getDays() {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = dayRepository.getAllDays()
                if (result != null) {
                    log("result $result ")
                    val days = result
                    days?.let { postDays(days) }
                } else {
                    log("Failed to fetch days")
                }
            } catch (e: Exception) {
                log("Unexpected error: ${e.message}")
            }
        }

    }

    fun clearData() {
        _taskList.postValue(mutableListOf())
    }

    private fun postDays(days: List<DayEntity>?) {
        val list = mutableSetOf<CalendarDay>()

        days?.forEach { day ->
            val date = Date(day.day)
            val calendarDay = CalendarDay(date)
            list.add(calendarDay)
        }

        _daysList.postValue(list.toHashSet())
    }


    /**
     * Converts a list of [CalenderActivity] into [TaskEntity] objects for a given day.
     */
    fun toTaskEntities(activities: List<CalenderActivity>, day: String): List<TaskEntity> =
        activities.map { activity ->
            TaskEntity(
                day = day,
                image = activity.image,
                nameTask = activity.nameTask,
                isCompleted = false,
                description = activity.description
            )
        }


    private fun addTasks(tasks: List<TaskEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = taskRepository.addTasks(tasks)
                if (result != null) {
                    val ids = result
                    log("the ids are $ids")
                } else {
                    log("Failed to fetch days:")
                }
            } catch (e: Exception) {
                log("Unexpected error: ${e.message}")
            }
        }
    }

    fun getTasks(day: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = taskRepository.getTasks(day)
                if (result != null) {
                    val tasks = result
                    tasks?.let { postTasks(it) }
                } else {
                    log("Failed to fetch task")
                }
            } catch (e: Exception) {
                log("Unexpected error: ${e.message}")
            }
        }
    }

    private fun postTasks(tasks: List<TaskEntity>) {
        _taskList.postValue(tasks)
    }

    /**
     * Updates a task  a task asynchronously via the use cases.
     */
    fun updateTask(task: TaskEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = taskRepository.updateTask(task)
                if (result != null) {
                    log("done")
                } else {
                    log("Failed to update task")
                }
            } catch (e: Exception) {
                log("Unexpected error: ${e.message}")
            }
        }

    }


    fun deleteTask(taskId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = taskRepository.deleteTask(taskId)
                if (result != null) {
                    log("done: ")
                } else {
                    log("Failed to delete task:")
                }
            } catch (e: Exception) {
                log("Unexpected error: ${e.message}")
            }
        }

    }


}