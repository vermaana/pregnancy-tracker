package com.anni.pregnancytracker.data.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKeys {
    val NAME = stringPreferencesKey("user_name")
    val AGE = intPreferencesKey("user_age")
    val LMP_DATE_ISO = stringPreferencesKey("lmp_date_iso")
    val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
}
