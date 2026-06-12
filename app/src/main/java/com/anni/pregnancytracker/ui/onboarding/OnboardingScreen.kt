package com.anni.pregnancytracker.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anni.pregnancytracker.R
import com.anni.pregnancytracker.ui.onboarding.components.LmpDatePicker
import com.anni.pregnancytracker.ui.onboarding.components.OnboardingTextField

private const val ENTER_ANIMATION_DURATION_MS = 600

@Composable
fun OnboardingScreen(onNavigateToHome: () -> Unit, viewModel: OnboardingViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    OnboardingContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToHome = onNavigateToHome,
    )
}

@Composable
internal fun OnboardingContent(
    uiState: OnboardingUiState,
    onEvent: (OnboardingEvent) -> Unit,
    onNavigateToHome: () -> Unit,
) {
    LaunchedEffect(uiState.isSubmitted) {
        if (uiState.isSubmitted) onNavigateToHome()
    }

    val visibleState = remember {
        MutableTransitionState(false).apply { targetState = true }
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(animationSpec = tween(ENTER_ANIMATION_DURATION_MS)) +
            slideInVertically(
                animationSpec = tween(ENTER_ANIMATION_DURATION_MS),
                initialOffsetY = { it / 4 },
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Spacer(modifier = Modifier.height(56.dp))

            Text(
                text = "Welcome",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Let's get to know you",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            OnboardingTextField(
                label = stringResource(R.string.label_name),
                value = uiState.name,
                onValueChange = { onEvent(OnboardingEvent.NameChanged(it)) },
                error = uiState.nameError,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next,
                ),
            )

            OnboardingTextField(
                label = stringResource(R.string.label_age),
                value = uiState.age,
                onValueChange = { onEvent(OnboardingEvent.AgeChanged(it)) },
                error = uiState.ageError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
            )

            LmpDatePicker(
                selectedDate = uiState.lmpDate,
                onDateSelected = { onEvent(OnboardingEvent.LmpDateSelected(it)) },
                error = uiState.lmpDateError,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onEvent(OnboardingEvent.SubmitClicked) },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.align(Alignment.CenterVertically),
                    )
                } else {
                    Text(
                        text = stringResource(R.string.button_begin_journey),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }

            Text(
                text = "Due date is estimated at 40 weeks from your LMP",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
