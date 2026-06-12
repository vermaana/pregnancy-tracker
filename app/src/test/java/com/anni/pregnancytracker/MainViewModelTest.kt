package com.anni.pregnancytracker

import com.anni.pregnancytracker.domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        userRepository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial value is null before first emission`() {
        every { userRepository.isOnboardingComplete() } returns flowOf(true)
        viewModel = MainViewModel(userRepository)

        assertNull(viewModel.isOnboardingComplete.value)
    }

    @Test
    fun `isOnboardingComplete reflects true from repository`() = runTest {
        every { userRepository.isOnboardingComplete() } returns flowOf(true)
        viewModel = MainViewModel(userRepository)

        val job = launch { viewModel.isOnboardingComplete.collect {} }
        testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        assertEquals(true, viewModel.isOnboardingComplete.value)
    }

    @Test
    fun `isOnboardingComplete reflects false from repository`() = runTest {
        every { userRepository.isOnboardingComplete() } returns flowOf(false)
        viewModel = MainViewModel(userRepository)

        val job = launch { viewModel.isOnboardingComplete.collect {} }
        testDispatcher.scheduler.advanceUntilIdle()
        job.cancel()

        assertEquals(false, viewModel.isOnboardingComplete.value)
    }
}
