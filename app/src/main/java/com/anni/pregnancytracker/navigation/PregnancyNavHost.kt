package com.anni.pregnancytracker.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.anni.pregnancytracker.ui.home.HomeScreen
import com.anni.pregnancytracker.ui.onboarding.OnboardingScreen

private const val TRANSITION_DURATION_MS = 400

@Composable
fun PregnancyNavHost(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(
            route = Screen.Onboarding.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(TRANSITION_DURATION_MS),
                ) + fadeOut(animationSpec = tween(TRANSITION_DURATION_MS))
            },
        ) {
            OnboardingScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
            )
        }

        composable(
            route = Screen.Home.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(TRANSITION_DURATION_MS),
                ) + fadeIn(animationSpec = tween(TRANSITION_DURATION_MS))
            },
        ) {
            HomeScreen()
        }
    }
}
