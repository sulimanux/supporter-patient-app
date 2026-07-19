package com.app.sanad.users.patient.dailyprogram.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.app.sanad.app.data.AppDatabase
import com.app.sanad.users.patient.dailyprogram.data.repo.DailyProgramRepository
import com.app.sanad.users.patient.dailyprogram.data.daos.DayTaskDao
import com.app.sanad.users.patient.dailyprogram.presentaion.DailyProgramViewModel
import com.app.sanad.util.SharedPreferencesManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Di {


    @Provides
    @Singleton
    fun provideDayTaskViewModel(                                    dailyProgramRepository: DailyProgramRepository,
                                  sharedPreferences: SharedPreferencesManager)= DailyProgramViewModel(dailyProgramRepository,sharedPreferences)





    @Provides
    @Singleton
    fun provideDayTaskRepository(dao: DayTaskDao, firestore: FirebaseFirestore, sharedPreferences: SharedPreferencesManager)  = DailyProgramRepository(dao, firestore, sharedPreferences)

    @Provides
    @Singleton
    fun provideDayTaskDao(db: AppDatabase) = db.dayTaskDao()


}