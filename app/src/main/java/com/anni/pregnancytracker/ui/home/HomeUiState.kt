package com.anni.pregnancytracker.ui.home

import com.anni.pregnancytracker.domain.model.CalendarWeek
import com.anni.pregnancytracker.domain.model.UserProfile
import java.time.LocalDate

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Error(val message: String) : HomeUiState
    data class Success(
        val profile: UserProfile,
        val calendar: List<CalendarWeek>,
        val dueDate: LocalDate,
        val todayGestationalWeek: Int,
    ) : HomeUiState
}
