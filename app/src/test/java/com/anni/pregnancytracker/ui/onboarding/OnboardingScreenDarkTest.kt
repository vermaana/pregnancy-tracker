package com.anni.pregnancytracker.ui.onboarding

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.resources.NightMode
import com.anni.pregnancytracker.ui.theme.PregnancyTrackerTestTheme
import java.time.LocalDate
import org.junit.Rule
import org.junit.Test

class OnboardingScreenDarkTest {

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5.copy(nightMode = NightMode.NIGHT))

    @Test
    fun onboarding_filled_dark() {
        paparazzi.snapshot {
            PregnancyTrackerTestTheme(darkTheme = true) {
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
}
