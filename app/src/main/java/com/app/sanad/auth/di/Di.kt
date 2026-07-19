package com.app.sanad.auth.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.app.sanad.auth.data.repo.AuthRepo
import com.app.sanad.auth.presentation.AuthViewModel
import com.app.sanad.users.patient.dailyprogram.data.repo.DailyProgramRepository
import com.app.sanad.util.SharedPreferencesManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Di {

//    @Provides
//    @Singleton
//    fun provideAuthViewModel(
//        sharedPreferences: SharedPreferencesManager,
//        dailyProgramRepo: DailyProgramRepository,
//        authRepo: AuthRepo,
//    ) = AuthViewModel(sharedPreferences, dailyProgramRepo, authRepo)


    @Provides
    @Singleton
    fun provideAuthRepo(
        firestore: FirebaseFirestore,
        fireAuth: FirebaseAuth,
        firebaseMessaging: FirebaseMessaging,
        sharedPreferences: SharedPreferencesManager
    ) = AuthRepo(firestore, fireAuth, firebaseMessaging, sharedPreferences)


}