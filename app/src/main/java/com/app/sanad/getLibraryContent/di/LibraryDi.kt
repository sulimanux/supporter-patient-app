package com.app.sanad.getLibraryContent.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.app.sanad.getLibraryContent.data.LibraryContentRepo
import com.app.sanad.getLibraryContent.data.LibraryDao
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LibraryDi {
   @Provides
    @Singleton
    fun provideLibraryContentRepo(firestore: FirebaseFirestore,libraryDao: LibraryDao): LibraryContentRepo {
        return LibraryContentRepo(firestore,libraryDao)
    }


}