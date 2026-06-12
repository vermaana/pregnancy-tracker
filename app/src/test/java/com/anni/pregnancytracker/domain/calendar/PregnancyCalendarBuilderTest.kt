package com.anni.pregnancytracker.domain.calendar

import com.anni.pregnancytracker.core.Constants
import java.time.LocalDate
import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PregnancyCalendarBuilderTest {

    private lateinit var builder: PregnancyCalendarBuilder
    private val lmpDate = LocalDate.of(2025, 11, 1)
    private val today = LocalDate.of(2025, 12, 15)

    @Before
    fun setUp() {
        builder = PregnancyCalendarBuilder()
    }

    @Test
    fun `gestationalWeekFor day zero is week 1`() {
        assertEquals(1, builder.gestationalWeekFor(lmpDate, lmpDate))
    }

    @Test
    fun `gestationalWeekFor day 6 is still week 1`() {
        assertEquals(1, builder.gestationalWeekFor(lmpDate.plusDays(6), lmpDate))
    }

    @Test
    fun `gestationalWeekFor day 7 is week 2`() {
        assertEquals(2, builder.gestationalWeekFor(lmpDate.plusDays(7), lmpDate))
    }

    @Test
    fun `lmpDay is flagged on correct date`() {
        val calendar = builder.build(lmpDate, today)
        val lmpDay = calendar.flatMap { it.days }.first { it.date == lmpDate }
        assertTrue(lmpDay.isLmpDay)
    }

    @Test
    fun `ovulation day is flagged on lmpDate plus 14`() {
        val ovulationDate = lmpDate.plusDays(Constants.OVULATION_OFFSET_DAYS)
        val calendar = builder.build(lmpDate, today)
        val ovulationDay = calendar.flatMap { it.days }.first { it.date == ovulationDate }
        assertTrue(ovulationDay.isOvulationDay)
    }

    @Test
    fun `today is flagged on matching date`() {
        val calendar = builder.build(lmpDate, today)
        val todayDays = calendar.flatMap { it.days }.filter { it.isToday }
        assertEquals(1, todayDays.size)
        assertEquals(today, todayDays.first().date)
    }

    @Test
    fun `calendar spans at least 40 weeks`() {
        val calendar = builder.build(lmpDate, today)
        assertTrue(calendar.size >= Constants.GESTATIONAL_WEEKS_TOTAL)
    }

    @Test
    fun `each week has exactly 7 days`() {
        val calendar = builder.build(lmpDate, today)
        assertTrue(calendar.all { it.days.size == Constants.DAYS_IN_WEEK })
    }

    @Test
    fun `days before lmpDate have isCurrentMonth false`() {
        val calendar = builder.build(lmpDate, today)
        val paddingDays = calendar.flatMap { it.days }.filter { it.date.isBefore(lmpDate) }
        assertTrue(paddingDays.isNotEmpty())
        assertTrue(paddingDays.all { !it.isCurrentMonth })
    }

    @Test
    fun `days after due date have isCurrentMonth false`() {
        val dueDate = lmpDate.plusWeeks(Constants.GESTATIONAL_WEEKS_TOTAL.toLong())
        val calendar = builder.build(lmpDate, today)
        val overflowDays = calendar.flatMap { it.days }.filter { it.date.isAfter(dueDate) }
        assertTrue(overflowDays.all { !it.isCurrentMonth })
    }

    @Test
    fun `week number on first row equals gestational week 1`() {
        val calendar = builder.build(lmpDate, today)
        assertEquals(1, calendar.first().weekNumber)
    }

    @Test
    fun `gestationalWeek on lmpDate is 1`() {
        val calendar = builder.build(lmpDate, today)
        val lmpDay = calendar.flatMap { it.days }.first { it.date == lmpDate }
        assertEquals(1, lmpDay.gestationalWeek)
    }

    @Test
    fun `lmpDay is not flagged on other dates`() {
        val calendar = builder.build(lmpDate, today)
        val nonLmpDays = calendar.flatMap { it.days }.filter { it.date != lmpDate }
        assertFalse(nonLmpDays.any { it.isLmpDay })
    }

    @Test
    fun `only one ovulation day exists`() {
        val calendar = builder.build(lmpDate, today)
        val ovulationDays = calendar.flatMap { it.days }.filter { it.isOvulationDay }
        assertEquals(1, ovulationDays.size)
    }

    // buildForMonth tests

    @Test
    fun `buildForMonth returns only weeks covering that month`() {
        val yearMonth = YearMonth.of(2025, 12)
        val weeks = builder.buildForMonth(lmpDate, today, yearMonth)
        assertTrue(weeks.isNotEmpty())
        val allDays = weeks.flatMap { it.days }
        assertTrue(allDays.any { it.date.month.value == 12 && it.date.year == 2025 })
    }

    @Test
    fun `buildForMonth each week has exactly 7 days`() {
        val weeks = builder.buildForMonth(lmpDate, today, YearMonth.of(2025, 12))
        assertTrue(weeks.all { it.days.size == Constants.DAYS_IN_WEEK })
    }

    @Test
    fun `buildForMonth isCurrentMonth true only for days in the requested month`() {
        val yearMonth = YearMonth.of(2025, 12)
        val weeks = builder.buildForMonth(lmpDate, today, yearMonth)
        val allDays = weeks.flatMap { it.days }
        assertTrue(
            allDays.all { day ->
                val inMonth = day.date.month.value == 12 && day.date.year == 2025
                day.isCurrentMonth == inMonth
            },
        )
    }

    @Test
    fun `buildForMonth days before lmpDate are disabled`() {
        val yearMonth = YearMonth.of(2025, 11)
        val weeks = builder.buildForMonth(lmpDate, today, yearMonth)
        val daysBeforeLmp = weeks.flatMap { it.days }.filter { it.date.isBefore(lmpDate) }
        assertTrue(daysBeforeLmp.isNotEmpty())
        assertTrue(daysBeforeLmp.all { it.isDisabled })
    }

    @Test
    fun `buildForMonth days after today are disabled`() {
        val yearMonth = YearMonth.of(2025, 12)
        val weeks = builder.buildForMonth(lmpDate, today, yearMonth)
        val daysAfterToday = weeks.flatMap { it.days }.filter { it.date.isAfter(today) && it.isCurrentMonth }
        assertTrue(daysAfterToday.isNotEmpty())
        assertTrue(daysAfterToday.all { it.isDisabled })
    }

    @Test
    fun `buildForMonth lmpDate and today are not disabled`() {
        val yearMonth = YearMonth.of(2025, 12)
        val weeks = builder.buildForMonth(lmpDate, today, yearMonth)
        val allDays = weeks.flatMap { it.days }
        val todayDay = allDays.firstOrNull { it.isToday }
        if (todayDay != null) assertFalse(todayDay.isDisabled)
    }

    @Test
    fun `buildForMonth lmpDay is flagged on correct date`() {
        val yearMonth = YearMonth.of(2025, 11)
        val weeks = builder.buildForMonth(lmpDate, today, yearMonth)
        val lmpDay = weeks.flatMap { it.days }.firstOrNull { it.date == lmpDate }
        assertTrue(lmpDay?.isLmpDay == true)
    }
}
