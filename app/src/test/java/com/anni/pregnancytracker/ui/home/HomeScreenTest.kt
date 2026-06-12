package com.anni.pregnancytracker.ui.home

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.anni.pregnancytracker.domain.calendar.PregnancyCalendarBuilder
import com.anni.pregnancytracker.domain.model.UserProfile
import com.anni.pregnancytracker.ui.theme.PregnancyTrackerTestTheme
import java.time.LocalDate
import java.time.YearMonth
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    private val lmpDate = LocalDate.of(2025, 11, 1)
    private val today = LocalDate.of(2025, 12, 15)
    private val profile = UserProfile("Alice", 30, lmpDate)
    private val displayedMonth = YearMonth.of(2025, 12)
    private val monthCalendar = PregnancyCalendarBuilder().buildForMonth(lmpDate, today, displayedMonth)
    private val dueDate = lmpDate.plusWeeks(40)

    @Test
    fun home_loading() {
        paparazzi.snapshot {
            PregnancyTrackerTestTheme {
                HomeContent(
                    uiState = HomeUiState.Loading,
                    onNavigatePrev = {},
                    onNavigateNext = {},
                )
            }
        }
    }

    @Test
    fun home_error() {
        paparazzi.snapshot {
            PregnancyTrackerTestTheme {
                HomeContent(
                    uiState = HomeUiState.Error("Profile not found"),
                    onNavigatePrev = {},
                    onNavigateNext = {},
                )
            }
        }
    }

    @Test
    fun home_success() {
        paparazzi.snapshot {
            PregnancyTrackerTestTheme {
                HomeContent(
                    uiState = HomeUiState.Success(
                        profile = profile,
                        monthCalendar = monthCalendar,
                        dueDate = dueDate,
                        todayGestationalWeek = 7,
                        displayedMonth = displayedMonth,
                        canNavigatePrev = true,
                        canNavigateNext = false,
                    ),
                    onNavigatePrev = {},
                    onNavigateNext = {},
                )
            }
        }
    }
}
