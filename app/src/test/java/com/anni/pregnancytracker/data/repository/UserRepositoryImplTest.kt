package com.anni.pregnancytracker.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.anni.pregnancytracker.data.local.UserPreferencesKeys
import com.anni.pregnancytracker.domain.model.UserProfile
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

private class FakePreferencesDataStore(initial: Preferences = emptyPreferences()) : DataStore<Preferences> {
    private val _data = MutableStateFlow(initial)
    override val data: Flow<Preferences> get() = _data
    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        val updated = transform(_data.value)
        _data.value = updated
        return updated
    }
}

class UserRepositoryImplTest {

    @Test
    fun `getUserProfile returns null when no data stored`() = runTest {
        val repo = UserRepositoryImpl(FakePreferencesDataStore())
        assertNull(repo.getUserProfile().first())
    }

    @Test
    fun `getUserProfile returns profile when all keys present`() = runTest {
        val store = FakePreferencesDataStore()
        val repo = UserRepositoryImpl(store)
        val profile = UserProfile("Alice", 30, LocalDate.of(2025, 11, 1))

        repo.saveUserProfile(profile)

        assertEquals(profile, repo.getUserProfile().first())
    }

    @Test
    fun `getUserProfile returns null when name key missing`() = runTest {
        val initial = emptyPreferences().toMutablePreferences().apply {
            set(UserPreferencesKeys.AGE, 30)
            set(UserPreferencesKeys.LMP_DATE_ISO, "2025-11-01")
        }.toPreferences()
        val repo = UserRepositoryImpl(FakePreferencesDataStore(initial))

        assertNull(repo.getUserProfile().first())
    }

    @Test
    fun `isOnboardingComplete returns false when key absent`() = runTest {
        val repo = UserRepositoryImpl(FakePreferencesDataStore())
        assertFalse(repo.isOnboardingComplete().first())
    }

    @Test
    fun `isOnboardingComplete returns true after markOnboardingComplete`() = runTest {
        val store = FakePreferencesDataStore()
        val repo = UserRepositoryImpl(store)

        repo.markOnboardingComplete()

        assertTrue(repo.isOnboardingComplete().first())
    }

    @Test
    fun `saveUserProfile writes all three keys`() = runTest {
        val store = FakePreferencesDataStore()
        val repo = UserRepositoryImpl(store)
        val profile = UserProfile("Alice", 30, LocalDate.of(2025, 11, 1))

        repo.saveUserProfile(profile)

        val prefs = store.data.first()
        assertEquals("Alice", prefs[UserPreferencesKeys.NAME])
        assertEquals(30, prefs[UserPreferencesKeys.AGE])
        assertEquals("2025-11-01", prefs[UserPreferencesKeys.LMP_DATE_ISO])
    }

    @Test
    fun `markOnboardingComplete sets onboarding key to true`() = runTest {
        val store = FakePreferencesDataStore()
        val repo = UserRepositoryImpl(store)

        repo.markOnboardingComplete()

        val prefs = store.data.first()
        assertEquals(true, prefs[UserPreferencesKeys.ONBOARDING_COMPLETE])
    }
}
