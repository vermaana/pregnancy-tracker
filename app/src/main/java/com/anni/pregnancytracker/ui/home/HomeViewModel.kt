package com.anni.pregnancytracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anni.pregnancytracker.core.Constants
import com.anni.pregnancytracker.domain.calendar.PregnancyCalendarBuilder
import com.anni.pregnancytracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeViewModel @Inject constructor(
    userRepository: UserRepository,
    private val calendarBuilder: PregnancyCalendarBuilder,
) : ViewModel() {

    private val displayedMonth = MutableStateFlow(YearMonth.now())

    val uiState: StateFlow<HomeUiState> = combine(
        userRepository.getUserProfile(),
        displayedMonth,
    ) { profile, month ->
        if (profile == null) {
            HomeUiState.Error("Profile not found")
        } else {
            val today = LocalDate.now()
            val lmpMonth = YearMonth.from(profile.lmpDate)
            val todayMonth = YearMonth.from(today)
            HomeUiState.Success(
                profile = profile,
                monthCalendar = calendarBuilder.buildForMonth(profile.lmpDate, today, month),
                dueDate = profile.lmpDate.plusWeeks(Constants.GESTATIONAL_WEEKS_TOTAL.toLong()),
                todayGestationalWeek = calendarBuilder.gestationalWeekFor(today, profile.lmpDate),
                displayedMonth = month,
                canNavigatePrev = month.isAfter(lmpMonth),
                canNavigateNext = month.isBefore(todayMonth),
            )
        }
    }
        .catch { emit(HomeUiState.Error("Something went wrong")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Constants.STOP_TIMEOUT_MS),
            initialValue = HomeUiState.Loading,
        )

    fun navigateMonth(delta: Int) {
        displayedMonth.update { it.plusMonths(delta.toLong()) }
    }
}
