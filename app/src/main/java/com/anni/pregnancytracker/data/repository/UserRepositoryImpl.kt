package com.anni.pregnancytracker.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.anni.pregnancytracker.data.local.UserPreferencesKeys
import com.anni.pregnancytracker.domain.model.UserProfile
import com.anni.pregnancytracker.domain.repository.UserRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>) : UserRepository {

    override fun getUserProfile(): Flow<UserProfile?> = dataStore.data.map { prefs ->
        val name = prefs[UserPreferencesKeys.NAME] ?: return@map null
        val age = prefs[UserPreferencesKeys.AGE] ?: return@map null
        val lmpIso = prefs[UserPreferencesKeys.LMP_DATE_ISO] ?: return@map null
        UserProfile(
            name = name,
            age = age,
            lmpDate = LocalDate.parse(lmpIso),
        )
    }

    override fun isOnboardingComplete(): Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[UserPreferencesKeys.ONBOARDING_COMPLETE] ?: false
    }

    override suspend fun saveUserProfile(profile: UserProfile) {
        dataStore.edit { prefs ->
            prefs[UserPreferencesKeys.NAME] = profile.name
            prefs[UserPreferencesKeys.AGE] = profile.age
            prefs[UserPreferencesKeys.LMP_DATE_ISO] = profile.lmpDate.toString()
        }
    }

    override suspend fun markOnboardingComplete() {
        dataStore.edit { prefs ->
            prefs[UserPreferencesKeys.ONBOARDING_COMPLETE] = true
        }
    }
}
