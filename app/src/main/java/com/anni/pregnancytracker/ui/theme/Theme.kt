package com.anni.pregnancytracker.ui.theme

import android.app.Activity
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Rose,
    onPrimary = Cream,
    primaryContainer = Blush,
    onPrimaryContainer = DeepRose,
    secondary = Mauve,
    onSecondary = Cream,
    secondaryContainer = MauveSurface,
    onSecondaryContainer = DeepRose,
    background = Cream,
    onBackground = TextPrimary,
    surface = Cream,
    onSurface = TextPrimary,
    surfaceVariant = BlushLight,
    onSurfaceVariant = TextSecondary,
    outline = DividerPink,
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkSurface,
    primaryContainer = DeepRose,
    onPrimaryContainer = Cream,
    secondary = Blush,
    onSecondary = DarkSurface,
    background = DarkSurface,
    onBackground = Cream,
    surface = DarkSurface,
    onSurface = Cream,
    surfaceVariant = TextPrimary,
    onSurfaceVariant = BlushLight,
)

@Composable
fun PregnancyTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    useFallbackFonts: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val typography = if (useFallbackFonts) {
        buildTypography(serifFamily = FontFamily.Serif, sansFamily = FontFamily.SansSerif)
    } else {
        Typography
    }

    val view = LocalView.current
    val activity = view.context as? Activity
    if (!view.isInEditMode && activity != null) {
        SideEffect {
            WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content,
    )
}

@VisibleForTesting
@Composable
fun PregnancyTrackerTestTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    PregnancyTrackerTheme(darkTheme = darkTheme, useFallbackFonts = true, content = content)
}
