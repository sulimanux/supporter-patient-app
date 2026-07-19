package com.app.sanad.app.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.app.sanad.app.data.AppDatabase
import com.app.sanad.chatting.data.repo.ChattingRepo
import com.app.sanad.getLibraryContent.data.LibraryDao
import com.app.sanad.users.patient.moodTracking.data.repo.MoodTrackingRepository
import com.app.sanad.util.ClearDataSession
import com.app.sanad.util.NetworkMonitor
import com.app.sanad.util.SharedPreferencesManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

 @Provides
 @Singleton
 fun provideSharedPreferencesManager(@ApplicationContext context: Context) =
     SharedPreferencesManager(context)



 @Provides
 @Singleton
 fun provideFirebaseAnalytics(sharedPreferencesManager: SharedPreferencesManager): FirebaseAnalytics {
  val userId = sharedPreferencesManager.getUserProfile().id ?: "idNull"
  val userName = sharedPreferencesManager.getUserProfile().name ?: "nameNull"
  val firebaseAnalytics = Firebase.analytics
  firebaseAnalytics.setUserId(userId)
  firebaseAnalytics.setUserProperty("user_name", userName)
  return firebaseAnalytics
 }


 @Provides
 @Singleton
 fun provideNetworkMonitor(@ApplicationContext context: Context) = NetworkMonitor(context)

 @Provides
 @Singleton
 fun provideFirebaseFireStore() = FirebaseFirestore.getInstance()

 @Provides
 @Singleton
 fun provideFirebaseStorage() = FirebaseStorage.getInstance()

 @Provides
 @Singleton
 fun provideFirebaseAuth() = FirebaseAuth.getInstance()

 @Provides
 @Singleton
 fun provideFirebaseMessaging() = FirebaseMessaging.getInstance()

 @Provides
 @Singleton
 fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
  return AppDatabase.Companion.getDatabase(context)
 }

 @Provides
 fun provideLibraryDao(appDatabase: AppDatabase): LibraryDao {
  return appDatabase.libraryDao()
 }



 @Provides
 @Singleton
 fun provideClearDataSession(moodTrackingRepository: MoodTrackingRepository, chattingRepo: ChattingRepo,): ClearDataSession {
  return ClearDataSession(moodTrackingRepository, chattingRepo)
 }




}