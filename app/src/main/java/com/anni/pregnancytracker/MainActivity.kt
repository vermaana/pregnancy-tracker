package com.anni.pregnancytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.anni.pregnancytracker.navigation.PregnancyNavHost
import com.anni.pregnancytracker.navigation.Screen
import com.anni.pregnancytracker.ui.theme.Cream
import com.anni.pregnancytracker.ui.theme.PregnancyTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PregnancyTrackerTheme {
                val isOnboardingComplete by viewModel.isOnboardingComplete
                    .collectAsStateWithLifecycle()

                isOnboardingComplete?.let { complete ->
                    val startDestination =
                        if (complete) Screen.Home.route else Screen.Onboarding.route
                    val navController = rememberNavController()
                    PregnancyNavHost(
                        navController = navController,
                        startDestination = startDestination,
                    )
                } ?: Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Cream),
                )
            }
        }
    }
}
