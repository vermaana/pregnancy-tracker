package com.anni.pregnancytracker.ui.onboarding

import com.anni.pregnancytracker.domain.model.UserProfile
import com.anni.pregnancytracker.domain.repository.UserRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: OnboardingViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        userRepository = mockk()
        viewModel = OnboardingViewModel(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is default`() {
        assertEquals(OnboardingUiState(), viewModel.uiState.value)
    }

    @Test
    fun `NameChanged updates name and clears error`() {
        viewModel.onEvent(OnboardingEvent.SubmitClicked)
        viewModel.onEvent(OnboardingEvent.NameChanged("Alice"))

        assertEquals("Alice", viewModel.uiState.value.name)
        assertNull(viewModel.uiState.value.nameError)
    }

    @Test
    fun `AgeChanged updates age and clears error`() {
        viewModel.onEvent(OnboardingEvent.AgeChanged("abc"))
        viewModel.onEvent(OnboardingEvent.SubmitClicked)
        viewModel.onEvent(OnboardingEvent.AgeChanged("30"))

        assertEquals("30", viewModel.uiState.value.age)
        assertNull(viewModel.uiState.value.ageError)
    }

    @Test
    fun `LmpDateSelected updates date and clears error`() {
        val date = LocalDate.of(2025, 11, 1)
        viewModel.onEvent(OnboardingEvent.LmpDateSelected(date))

        assertEquals(date, viewModel.uiState.value.lmpDate)
        assertNull(viewModel.uiState.value.lmpDateError)
    }

    @Test
    fun `submit with blank name sets nameError`() {
        viewModel.onEvent(OnboardingEvent.AgeChanged("30"))
        viewModel.onEvent(OnboardingEvent.LmpDateSelected(LocalDate.now().minusDays(1)))
        viewModel.onEvent(OnboardingEvent.SubmitClicked)

        assertNotNull(viewModel.uiState.value.nameError)
    }

    @Test
    fun `submit with non-numeric age sets ageError`() {
        viewModel.onEvent(OnboardingEvent.NameChanged("Alice"))
        viewModel.onEvent(OnboardingEvent.AgeChanged("abc"))
        viewModel.onEvent(OnboardingEvent.LmpDateSelected(LocalDate.now().minusDays(1)))
        viewModel.onEvent(OnboardingEvent.SubmitClicked)

        assertNotNull(viewModel.uiState.value.ageError)
    }

    @Test
    fun `submit with age out of range sets ageError`() {
        viewModel.onEvent(OnboardingEvent.NameChanged("Alice"))
        viewModel.onEvent(OnboardingEvent.AgeChanged("5"))
        viewModel.onEvent(OnboardingEvent.LmpDateSelected(LocalDate.now().minusDays(1)))
        viewModel.onEvent(OnboardingEvent.SubmitClicked)

        assertNotNull(viewModel.uiState.value.ageError)
    }

    @Test
    fun `submit with future lmp sets lmpDateError`() {
        viewModel.onEvent(OnboardingEvent.NameChanged("Alice"))
        viewModel.onEvent(OnboardingEvent.AgeChanged("30"))
        viewModel.onEvent(OnboardingEvent.LmpDateSelected(LocalDate.now().plusDays(1)))
        viewModel.onEvent(OnboardingEvent.SubmitClicked)

        assertNotNull(viewModel.uiState.value.lmpDateError)
    }

    @Test
    fun `submit with null lmpDate sets lmpDateError`() {
        viewModel.onEvent(OnboardingEvent.NameChanged("Alice"))
        viewModel.onEvent(OnboardingEvent.AgeChanged("30"))
        viewModel.onEvent(OnboardingEvent.SubmitClicked)

        assertNotNull(viewModel.uiState.value.lmpDateError)
    }

    @Test
    fun `valid submit calls repository and sets isSubmitted`() = runTest {
        val lmpDate = LocalDate.now().minusDays(30)
        coJustRun { userRepository.saveUserProfile(any()) }
        coJustRun { userRepository.markOnboardingComplete() }

        viewModel.onEvent(OnboardingEvent.NameChanged("Alice"))
        viewModel.onEvent(OnboardingEvent.AgeChanged("30"))
        viewModel.onEvent(OnboardingEvent.LmpDateSelected(lmpDate))
        viewModel.onEvent(OnboardingEvent.SubmitClicked)

        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { userRepository.saveUserProfile(UserProfile("Alice", 30, lmpDate)) }
        coVerify { userRepository.markOnboardingComplete() }
        assertTrue(viewModel.uiState.value.isSubmitted)
    }

    @Test
    fun `submit does not call repository when validation fails`() = runTest {
        viewModel.onEvent(OnboardingEvent.SubmitClicked)

        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { userRepository.saveUserProfile(any()) }
    }
}
