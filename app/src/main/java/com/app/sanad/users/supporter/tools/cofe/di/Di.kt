package com.app.sanad.users.supporter.tools.cofe.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.app.sanad.users.supporter.tools.cofe.data.repo.Repository
import com.app.sanad.users.supporter.tools.cofe.presintaion.SupportCafeViewModel
import com.app.sanad.util.SharedPreferencesManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Di {

    @Provides
    @Singleton
    fun  provideRepository() = Repository()

    @Provides
    @Singleton
    fun provideSupportCafeViewModel(sharedPreferences: SharedPreferencesManager, repository: Repository) = SupportCafeViewModel(sharedPreferences,repository)
}