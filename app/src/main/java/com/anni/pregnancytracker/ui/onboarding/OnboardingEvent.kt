package com.anni.pregnancytracker.ui.onboarding

import java.time.LocalDate

sealed interface OnboardingEvent {
    data class NameChanged(val name: String) : OnboardingEvent
    data class AgeChanged(val age: String) : OnboardingEvent
    data class LmpDateSelected(val date: LocalDate) : OnboardingEvent
    object SubmitClicked : OnboardingEvent
}
