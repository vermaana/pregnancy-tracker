package com.anni.pregnancytracker.domain.model

import java.time.LocalDate

data class CalendarDay(
    val date: LocalDate,
    val gestationalWeek: Int,
    val isToday: Boolean,
    val isLmpDay: Boolean,
    val isOvulationDay: Boolean,
    val isCurrentMonth: Boolean,
    val isDisabled: Boolean,
)
