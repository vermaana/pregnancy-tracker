package com.anni.pregnancytracker.ui.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anni.pregnancytracker.ui.theme.TextSecondary

private val DAY_HEADERS = listOf("M", "T", "W", "T", "F", "S", "S")

@Composable
fun WeekDayHeaders(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        // Spacer matching the week-number column width
        Text(
            text = "",
            modifier = Modifier.width(32.dp),
        )
        DAY_HEADERS.forEach { day ->
            Text(
                text = day,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
