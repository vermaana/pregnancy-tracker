package com.anni.pregnancytracker.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.anni.pregnancytracker.data.repository.UserRepositoryImpl
import com.anni.pregnancytracker.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.userDataStore: DataStore<Preferences>
    by preferencesDataStore(name = "user_preferences")

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.userDataStore

    @Provides
    @Singleton
    fun provideUserRepository(dataStore: DataStore<Preferences>): UserRepository = UserRepositoryImpl(dataStore)
}
