package com.anni.pregnancytracker.ui.onboarding

import java.time.LocalDate

data class OnboardingUiState(
    val name: String = "",
    val age: String = "",
    val lmpDate: LocalDate? = null,
    val nameError: String? = null,
    val ageError: String? = null,
    val lmpDateError: String? = null,
    val isLoading: Boolean = false,
    val isSubmitted: Boolean = false,
)
