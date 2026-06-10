package com.anni.pregnancytracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anni.pregnancytracker.ui.home.components.CalendarWeekRow
import com.anni.pregnancytracker.ui.home.components.HomeHeader
import com.anni.pregnancytracker.ui.home.components.WeekDayHeaders
import com.anni.pregnancytracker.ui.theme.Cream
import com.anni.pregnancytracker.ui.theme.Rose
import com.anni.pregnancytracker.ui.theme.TextSecondary

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(uiState = uiState)
}

@Composable
internal fun HomeContent(uiState: HomeUiState) {
    when (uiState) {
        is HomeUiState.Loading -> HomeLoadingState()
        is HomeUiState.Error -> HomeErrorState(message = uiState.message)
        is HomeUiState.Success -> HomeSuccessContent(uiState = uiState)
    }
}

@Composable
private fun HomeLoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = Rose)
    }
}

@Composable
private fun HomeErrorState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
        )
    }
}

@Composable
private fun HomeSuccessContent(uiState: HomeUiState.Success) {
    val listState = rememberLazyListState()
    val todayWeekIndex = uiState.calendar.indexOfFirst { week ->
        week.days.any { it.isToday }
    }.coerceAtLeast(0)

    LaunchedEffect(Unit) {
        // +2 to account for the header and weekday-header items
        if (todayWeekIndex > 0) {
            listState.scrollToItem(todayWeekIndex + 2)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream),
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                HomeHeader(
                    profile = uiState.profile,
                    dueDate = uiState.dueDate,
                    todayGestationalWeek = uiState.todayGestationalWeek,
                )
            }
            item {
                WeekDayHeaders(modifier = Modifier.padding(horizontal = 8.dp))
            }
            items(uiState.calendar) { week ->
                CalendarWeekRow(week = week)
            }
        }
    }
}
