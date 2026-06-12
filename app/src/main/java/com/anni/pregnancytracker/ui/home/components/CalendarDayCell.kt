package com.anni.pregnancytracker.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anni.pregnancytracker.domain.model.CalendarDay
import com.anni.pregnancytracker.ui.theme.LmpAmber
import com.anni.pregnancytracker.ui.theme.OvulationSage

private data class DayCellStyle(
    val backgroundColor: Color,
    val textColor: Color,
    val badge: String?,
    val showDayNumber: Boolean,
)

private fun dayCellStyle(day: CalendarDay, primary: Color, onPrimary: Color, onSurface: Color): DayCellStyle = when {
    !day.isCurrentMonth -> DayCellStyle(Color.Transparent, Color.Transparent, null, false)
    day.isDisabled -> DayCellStyle(Color.Transparent, onSurface.copy(alpha = 0.3f), null, true)
    day.isLmpDay -> DayCellStyle(LmpAmber, Color.White, "LMP", true)
    day.isOvulationDay -> DayCellStyle(OvulationSage, Color.White, "OV", true)
    day.isToday -> DayCellStyle(primary, onPrimary, null, true)
    else -> DayCellStyle(Color.Transparent, onSurface, null, true)
}

@Composable
fun CalendarDayCell(day: CalendarDay, modifier: Modifier = Modifier) {
    val style = dayCellStyle(
        day = day,
        primary = MaterialTheme.colorScheme.primary,
        onPrimary = MaterialTheme.colorScheme.onPrimary,
        onSurface = MaterialTheme.colorScheme.onSurface,
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(style.backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (style.showDayNumber) day.date.dayOfMonth.toString() else "",
                style = MaterialTheme.typography.bodyMedium,
                color = style.textColor,
                textAlign = TextAlign.Center,
            )
            if (style.badge != null) {
                Text(
                    text = style.badge,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 7.sp),
                    color = style.textColor,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
