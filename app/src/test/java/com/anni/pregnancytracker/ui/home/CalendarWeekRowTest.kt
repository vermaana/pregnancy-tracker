package com.anni.pregnancytracker.ui.home

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.anni.pregnancytracker.domain.model.CalendarDay
import com.anni.pregnancytracker.domain.model.CalendarWeek
import com.anni.pregnancytracker.ui.home.components.CalendarWeekRow
import com.anni.pregnancytracker.ui.theme.PregnancyTrackerTestTheme
import java.time.LocalDate
import org.junit.Rule
import org.junit.Test

class CalendarWeekRowTest {

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    private fun makeWeek(
        startDate: LocalDate = LocalDate.of(2025, 12, 1),
        isToday: Int? = null,
        isLmp: Int? = null,
        isOvulation: Int? = null,
    ): CalendarWeek {
        val days = (0 until 7).map { i ->
            CalendarDay(
                date = startDate.plusDays(i.toLong()),
                gestationalWeek = 5,
                isToday = i == isToday,
                isLmpDay = i == isLmp,
                isOvulationDay = i == isOvulation,
                isCurrentMonth = true,
                isDisabled = false,
            )
        }
        return CalendarWeek(weekNumber = 5, days = days)
    }

    @Test
    fun weekRow_no_highlights() {
        paparazzi.snapshot {
            PregnancyTrackerTestTheme {
                CalendarWeekRow(week = makeWeek())
            }
        }
    }

    @Test
    fun weekRow_with_lmp_day() {
        paparazzi.snapshot {
            PregnancyTrackerTestTheme {
                CalendarWeekRow(week = makeWeek(isLmp = 3))
            }
        }
    }

    @Test
    fun weekRow_with_ovulation_day() {
        paparazzi.snapshot {
            PregnancyTrackerTestTheme {
                CalendarWeekRow(week = makeWeek(isOvulation = 2))
            }
        }
    }

    @Test
    fun weekRow_with_today() {
        paparazzi.snapshot {
            PregnancyTrackerTestTheme {
                CalendarWeekRow(week = makeWeek(isToday = 4))
            }
        }
    }

    @Test
    fun weekRow_with_all_highlights() {
        paparazzi.snapshot {
            PregnancyTrackerTestTheme {
                CalendarWeekRow(week = makeWeek(isToday = 5, isLmp = 1, isOvulation = 3))
            }
        }
    }
}
