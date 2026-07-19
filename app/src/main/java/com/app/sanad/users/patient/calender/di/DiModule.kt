package com.app.sanad.users.patient.calender.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.app.sanad.app.data.AppDatabase
import com.app.sanad.users.patient.calender.data.daos.DayDao
import com.app.sanad.users.patient.calender.data.daos.TaskDao
import com.app.sanad.users.patient.calender.data.repo.CalenderActivitiesRepo
import com.app.sanad.users.patient.calender.data.repo.DayRepository
import com.app.sanad.users.patient.calender.data.repo.TaskRepository
import com.app.sanad.users.patient.calender.presentaion.CalenderViewModel
import com.app.sanad.util.SharedPreferencesManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  DiModule {

    //ViewModels
    @Provides
    @Singleton
    fun provideCalenderViewModel(taskRepository: TaskRepository , dayRepository: DayRepository , calenderActivitiesRepo: CalenderActivitiesRepo) =
        CalenderViewModel(taskRepository = taskRepository, dayRepository = dayRepository, calenderRepo = calenderActivitiesRepo)

    //Repos
    @Provides
    @Singleton
    fun provideCalenderActivitiesRepository(sharedPreferences: SharedPreferencesManager) =
        CalenderActivitiesRepo(sharedPreferences)
    @Provides
    @Singleton
    fun providesDayRepository(dayDao: DayDao) = DayRepository(dayDao)
    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao) = TaskRepository(taskDao)


    //Dao
    @Provides
    @Singleton
    fun provideDayDao(appDatabase: AppDatabase) = appDatabase.dayDao()
    @Provides
    @Singleton
    fun provideTaskDao(appDatabase: AppDatabase) = appDatabase.taskDao()

}