package com.anni.pregnancytracker.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anni.pregnancytracker.core.Constants
import com.anni.pregnancytracker.domain.model.UserProfile
import com.anni.pregnancytracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.NameChanged ->
                _uiState.update { it.copy(name = event.name, nameError = null) }
            is OnboardingEvent.AgeChanged ->
                _uiState.update { it.copy(age = event.age, ageError = null) }
            is OnboardingEvent.LmpDateSelected ->
                _uiState.update { it.copy(lmpDate = event.date, lmpDateError = null) }
            is OnboardingEvent.SubmitClicked -> validateAndSubmit()
        }
    }

    private fun validateAndSubmit() {
        val state = _uiState.value

        val nameError = if (state.name.isBlank()) ERROR_NAME_REQUIRED else null

        val ageInt = state.age.toIntOrNull()
        val ageError = when {
            state.age.isBlank() -> ERROR_AGE_REQUIRED
            ageInt == null -> ERROR_AGE_INVALID
            ageInt < Constants.MIN_MATERNAL_AGE || ageInt > Constants.MAX_MATERNAL_AGE -> ERROR_AGE_RANGE
            else -> null
        }

        val lmpDateError = when {
            state.lmpDate == null -> ERROR_LMP_REQUIRED
            state.lmpDate.isAfter(LocalDate.now()) -> ERROR_LMP_FUTURE
            else -> null
        }

        if (nameError != null || ageError != null || lmpDateError != null) {
            _uiState.update {
                it.copy(
                    nameError = nameError,
                    ageError = ageError,
                    lmpDateError = lmpDateError,
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            userRepository.saveUserProfile(
                UserProfile(
                    name = state.name.trim(),
                    age = ageInt!!,
                    lmpDate = state.lmpDate!!,
                ),
            )
            userRepository.markOnboardingComplete()
            _uiState.update { it.copy(isLoading = false, isSubmitted = true) }
        }
    }

    private companion object {
        const val ERROR_NAME_REQUIRED = "Name is required"
        const val ERROR_AGE_REQUIRED = "Age is required"
        const val ERROR_AGE_INVALID = "Age must be a number"
        const val ERROR_AGE_RANGE = "Please enter a valid age"
        const val ERROR_LMP_REQUIRED = "Please select your LMP date"
        const val ERROR_LMP_FUTURE = "LMP date cannot be in the future"
    }
}
