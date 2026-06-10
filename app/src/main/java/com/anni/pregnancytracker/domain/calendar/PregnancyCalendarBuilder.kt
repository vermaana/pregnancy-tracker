package com.anni.pregnancytracker.domain.calendar

import com.anni.pregnancytracker.core.Constants
import com.anni.pregnancytracker.domain.model.CalendarDay
import com.anni.pregnancytracker.domain.model.CalendarWeek
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class PregnancyCalendarBuilder @Inject constructor() {

    fun build(lmpDate: LocalDate, today: LocalDate = LocalDate.now()): List<CalendarWeek> {
        val ovulationDate = lmpDate.plusDays(Constants.OVULATION_OFFSET_DAYS)
        val dueDate = lmpDate.plusWeeks(Constants.GESTATIONAL_WEEKS_TOTAL.toLong())

        val lmpMonday = lmpDate.with(DayOfWeek.MONDAY).let { monday ->
            if (monday.isAfter(lmpDate)) monday.minusWeeks(1) else monday
        }

        val weeks = mutableListOf<CalendarWeek>()
        var weekStart = lmpMonday

        while (!weekStart.isAfter(dueDate)) {
            val days = (0 until Constants.DAYS_IN_WEEK).map { offset ->
                val day = weekStart.plusDays(offset.toLong())
                CalendarDay(
                    date = day,
                    gestationalWeek = gestationalWeekFor(day, lmpDate),
                    isToday = day == today,
                    isLmpDay = day == lmpDate,
                    isOvulationDay = day == ovulationDate,
                    isCurrentMonth = !day.isBefore(lmpDate) && !day.isAfter(dueDate),
                )
            }

            val weekNumber = days.firstOrNull { !it.date.isBefore(lmpDate) }?.gestationalWeek
                ?: gestationalWeekFor(weekStart, lmpDate)

            weeks.add(CalendarWeek(weekNumber = weekNumber, days = days))
            weekStart = weekStart.plusWeeks(1)
        }

        return weeks.toList()
    }

    fun gestationalWeekFor(date: LocalDate, lmpDate: LocalDate): Int {
        val daysDiff = ChronoUnit.DAYS.between(lmpDate, date)
        return (daysDiff / Constants.DAYS_IN_WEEK).toInt() + 1
    }
}
