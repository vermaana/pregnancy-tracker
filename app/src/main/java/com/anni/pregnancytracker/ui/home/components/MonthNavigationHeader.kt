package com.anni.pregnancytracker.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anni.pregnancytracker.ui.theme.DeepRose
import com.anni.pregnancytracker.ui.theme.TextSecondary
import java.time.YearMonth
import java.time.format.DateTimeFormatter

private val MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy")

@Composable
fun MonthNavigationHeader(
    displayedMonth: YearMonth,
    canNavigatePrev: Boolean,
    canNavigateNext: Boolean,
    onNavigatePrev: () -> Unit,
    onNavigateNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val monthLabel = remember(displayedMonth) { displayedMonth.format(MONTH_FORMATTER) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onNavigatePrev, enabled = canNavigatePrev) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Previous month",
                tint = if (canNavigatePrev) DeepRose else TextSecondary.copy(alpha = 0.3f),
            )
        }
        Text(
            text = monthLabel,
            style = MaterialTheme.typography.titleMedium,
            color = DeepRose,
        )
        IconButton(onClick = onNavigateNext, enabled = canNavigateNext) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next month",
                tint = if (canNavigateNext) DeepRose else TextSecondary.copy(alpha = 0.3f),
            )
        }
    }
}
