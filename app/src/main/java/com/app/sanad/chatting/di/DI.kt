package com.app.sanad.chatting.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.app.sanad.chatting.data.repo.ChattingRepo
import com.app.sanad.notifications.data.repos.NotificationsRepo
import com.app.sanad.util.SharedPreferencesManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DI {

    @Provides
    @Singleton
    fun provideChattingRepo(
        firestore: FirebaseFirestore,
        sharedPreferences: SharedPreferencesManager,
        notificationsRepo: NotificationsRepo
    ) = ChattingRepo(firestore, sharedPreferences , notificationsRepo )


}