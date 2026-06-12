package com.anni.pregnancytracker.ui.home

import com.anni.pregnancytracker.domain.calendar.PregnancyCalendarBuilder
import com.anni.pregnancytracker.domain.model.CalendarWeek
import com.anni.pregnancytracker.domain.model.UserProfile
import com.anni.pregnancytracker.domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var userRepository: UserRepository
    private lateinit var calendarBuilder: PregnancyCalendarBuilder
    private lateinit var viewModel: HomeViewModel

    private val lmpDate = LocalDate.of(2025, 11, 1)
    private val profile = UserProfile("Alice", 30, lmpDate)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        userRepository = mockk()
        calendarBuilder = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() {
        every { userRepository.getUserProfile() } returns flowOf(profile)
        every { calendarBuilder.buildForMonth(any(), any(), any()) } returns emptyList()
        every { calendarBuilder.gestationalWeekFor(any(), any()) } returns 1
        viewModel = HomeViewModel(userRepository, calendarBuilder)

        assertTrue(viewModel.uiState.value is HomeUiState.Loading)
    }

    @Test
    fun `with valid profile emits Success`() = runTest {
        val calendar = listOf<CalendarWeek>()
        every { userRepository.getUserProfile() } returns flowOf(profile)
        every { calendarBuilder.buildForMonth(any(), any(), any()) } returns calendar
        every { calendarBuilder.gestationalWeekFor(any(), lmpDate) } returns 8
        viewModel = HomeViewModel(userRepository, calendarBuilder)

        val job = launch { viewModel.uiState.collect {} }
        testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        val state = viewModel.uiState.value
        assertTrue(state is HomeUiState.Success)
        assertEquals(profile, (state as HomeUiState.Success).profile)
        assertEquals(8, state.todayGestationalWeek)
    }

    @Test
    fun `due date is lmpDate plus 40 weeks`() = runTest {
        every { userRepository.getUserProfile() } returns flowOf(profile)
        every { calendarBuilder.buildForMonth(any(), any(), any()) } returns emptyList()
        every { calendarBuilder.gestationalWeekFor(any(), any()) } returns 1
        viewModel = HomeViewModel(userRepository, calendarBuilder)

        val job = launch { viewModel.uiState.collect {} }
        testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        val state = viewModel.uiState.value as HomeUiState.Success
        assertEquals(lmpDate.plusWeeks(40), state.dueDate)
    }

    @Test
    fun `null profile emits Error`() = runTest {
        every { userRepository.getUserProfile() } returns flowOf(null)
        viewModel = HomeViewModel(userRepository, calendarBuilder)

        val job = launch { viewModel.uiState.collect {} }
        testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        assertTrue(viewModel.uiState.value is HomeUiState.Error)
    }

    @Test
    fun `exception in flow emits Error`() = runTest {
        every { userRepository.getUserProfile() } returns flow { throw IllegalStateException("DB error") }
        viewModel = HomeViewModel(userRepository, calendarBuilder)

        val job = launch { viewModel.uiState.collect {} }
        testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        assertTrue(viewModel.uiState.value is HomeUiState.Error)
    }

    @Test
    fun `navigateMonth updates displayedMonth in state`() = runTest {
        every { userRepository.getUserProfile() } returns flowOf(profile)
        every { calendarBuilder.buildForMonth(any(), any(), any()) } returns emptyList()
        every { calendarBuilder.gestationalWeekFor(any(), any()) } returns 1
        viewModel = HomeViewModel(userRepository, calendarBuilder)

        val job = launch { viewModel.uiState.collect {} }
        testDispatcher.scheduler.advanceUntilIdle()

        val initialMonth = (viewModel.uiState.value as HomeUiState.Success).displayedMonth
        viewModel.navigateMonth(-1)
        testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        val newMonth = (viewModel.uiState.value as HomeUiState.Success).displayedMonth
        assertEquals(initialMonth.minusMonths(1), newMonth)
    }
}
