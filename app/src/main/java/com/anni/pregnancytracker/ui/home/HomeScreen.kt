package com.anni.pregnancytracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anni.pregnancytracker.ui.home.components.CalendarWeekRow
import com.anni.pregnancytracker.ui.home.components.HomeHeader
import com.anni.pregnancytracker.ui.home.components.MonthNavigationHeader
import com.anni.pregnancytracker.ui.home.components.WeekDayHeaders

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(
        uiState = uiState,
        onNavigatePrev = { viewModel.navigateMonth(-1) },
        onNavigateNext = { viewModel.navigateMonth(1) },
    )
}

@Composable
internal fun HomeContent(uiState: HomeUiState, onNavigatePrev: () -> Unit, onNavigateNext: () -> Unit) {
    when (uiState) {
        is HomeUiState.Loading -> HomeLoadingState()
        is HomeUiState.Error -> HomeErrorState(message = uiState.message)
        is HomeUiState.Success -> HomeSuccessContent(
            uiState = uiState,
            onNavigatePrev = onNavigatePrev,
            onNavigateNext = onNavigateNext,
        )
    }
}

@Composable
private fun HomeLoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun HomeErrorState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun HomeSuccessContent(uiState: HomeUiState.Success, onNavigatePrev: () -> Unit, onNavigateNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        HomeHeader(
            profile = uiState.profile,
            dueDate = uiState.dueDate,
            todayGestationalWeek = uiState.todayGestationalWeek,
        )
        MonthNavigationHeader(
            displayedMonth = uiState.displayedMonth,
            canNavigatePrev = uiState.canNavigatePrev,
            canNavigateNext = uiState.canNavigateNext,
            onNavigatePrev = onNavigatePrev,
            onNavigateNext = onNavigateNext,
        )
        WeekDayHeaders()
        uiState.monthCalendar.forEach { week ->
            CalendarWeekRow(week = week)
        }
    }
}
