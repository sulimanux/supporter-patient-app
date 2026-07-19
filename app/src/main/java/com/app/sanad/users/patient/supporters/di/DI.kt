package com.app.sanad.users.patient.supporters.di
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.app.sanad.users.patient.supporters.data.repos.SupportersRepo
import com.app.sanad.util.SharedPreferencesManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DI {




    @Provides
    @Singleton
    fun provideSupportersRepo(
        firestore: FirebaseFirestore,
        sharedPreferences: SharedPreferencesManager
    ) = SupportersRepo(firestore,  sharedPreferences)


}