package com.app.sanad.users.patient.moodTracking.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.app.sanad.users.patient.dailyprogram.data.repo.DailyProgramRepository
import com.app.sanad.users.patient.moodTracking.data.repo.MoodTrackingRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Di {

    @Provides
    @Singleton
    fun provideMoodRepository(firestore: FirebaseFirestore , dailyProgramRepository: DailyProgramRepository) = MoodTrackingRepository(firestore, dailyProgramRepository)

}