package com.anni.pregnancytracker.ui.onboarding

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.anni.pregnancytracker.ui.theme.PregnancyTrackerTestTheme
import java.time.LocalDate
import org.junit.Rule
import org.junit.Test

class OnboardingScreenTest {

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun onboarding_default() {
        paparazzi.snapshot {
            PregnancyTrackerTestTheme {
                OnboardingContent(
                    uiState = OnboardingUiState(),
                    onEvent = {},
                    onNavigateToHome = {},
                )
            }
        }
    }

    @Test
    fun onboarding_filled() {
        paparazzi.snapshot {
            PregnancyTrackerTestTheme {
                OnboardingContent(
                    uiState = OnboardingUiState(
                        name = "Alice",
                        age = "30",
                        lmpDate = LocalDate.of(2025, 11, 1),
                    ),
                    onEvent = {},
                    onNavigateToHome = {},
                )
            }
        }
    }

    @Test
    fun onboarding_validation_errors() {
        paparazzi.snapshot {
            PregnancyTrackerTestTheme {
                OnboardingContent(
                    uiState = OnboardingUiState(
                        nameError = "Name is required",
                        ageError = "Age is required",
                        lmpDateError = "Please select your LMP date",
                    ),
                    onEvent = {},
                    onNavigateToHome = {},
                )
            }
        }
    }

    @Test
    fun onboarding_loading() {
        paparazzi.snapshot {
            PregnancyTrackerTestTheme {
                OnboardingContent(
                    uiState = OnboardingUiState(
                        name = "Alice",
                        age = "30",
                        lmpDate = LocalDate.of(2025, 11, 1),
                        isLoading = true,
                    ),
                    onEvent = {},
                    onNavigateToHome = {},
                )
            }
        }
    }
}
