package com.anni.pregnancytracker.domain.repository

import com.anni.pregnancytracker.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserProfile(): Flow<UserProfile?>
    fun isOnboardingComplete(): Flow<Boolean>
    suspend fun saveUserProfile(profile: UserProfile)
    suspend fun markOnboardingComplete()
}
