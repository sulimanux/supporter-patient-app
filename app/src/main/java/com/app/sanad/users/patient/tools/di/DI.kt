package com.app.sanad.users.patient.tools.di
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.app.sanad.users.patient.tools.breathing.data.repo.BreathingRepo
import com.app.sanad.users.patient.tools.gratitude.data.repo.GratitudeRepo
import com.app.sanad.users.patient.tools.supplications.data.SupplicationsRepo
import com.app.sanad.util.SharedPreferencesManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DI {

    @Provides
    @Singleton
    fun providerBreathingRepo(
    ) = BreathingRepo()

    @Provides
    @Singleton
    fun providerGratitudeRepo(sharedPreferencesManager: SharedPreferencesManager,firestore: FirebaseFirestore
    ) = GratitudeRepo(sharedPreferencesManager,firestore)

//    @Provides
//    @Singleton
//    fun  provideCofeViewModel(sharedPreferencesManager: SharedPreferencesManager,
//                              supportersRepo: SupportersRepo,
//                              cofeRepo: CofeRepo) = CofeViewModel(sharedPreferencesManager,supportersRepo,cofeRepo )
//

    @Provides
    @Singleton
    fun provideSupplicationsRepo(
        firestore: FirebaseFirestore,
        sharedPreferences: SharedPreferencesManager
    ) = SupplicationsRepo(firestore,  sharedPreferences)


}