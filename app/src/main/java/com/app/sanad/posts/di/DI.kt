package com.app.sanad.posts.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.app.sanad.notifications.data.repos.NotificationsRepo
import com.app.sanad.posts.data.repo.PostsRepo
import com.app.sanad.util.SharedPreferencesManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DI {

    @Provides
    @Singleton
    fun providePostsRepo(
        firestore: FirebaseFirestore,
        sharedPreferences: SharedPreferencesManager,
        notificationsRepo: NotificationsRepo,
        @ApplicationContext context: Context
    ) = PostsRepo(firestore, sharedPreferences, notificationsRepo, context)


}