package com.anni.pregnancytracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anni.pregnancytracker.core.Constants
import com.anni.pregnancytracker.domain.calendar.PregnancyCalendarBuilder
import com.anni.pregnancytracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel @Inject constructor(
    userRepository: UserRepository,
    private val calendarBuilder: PregnancyCalendarBuilder,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = userRepository.getUserProfile()
        .map { profile ->
            if (profile == null) {
                HomeUiState.Error("Profile not found")
            } else {
                val today = LocalDate.now()
                HomeUiState.Success(
                    profile = profile,
                    calendar = calendarBuilder.build(profile.lmpDate, today),
                    dueDate = profile.lmpDate.plusWeeks(Constants.GESTATIONAL_WEEKS_TOTAL.toLong()),
                    todayGestationalWeek = calendarBuilder.gestationalWeekFor(today, profile.lmpDate),
                )
            }
        }
        .catch { emit(HomeUiState.Error("Something went wrong")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Constants.STOP_TIMEOUT_MS),
            initialValue = HomeUiState.Loading,
        )
}
