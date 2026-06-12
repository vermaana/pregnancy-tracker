package com.anni.pregnancytracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.anni.pregnancytracker.R

private val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs,
)

private val playfairDisplayFont = GoogleFont("Playfair Display")
private val latoFont = GoogleFont("Lato")

val PlayfairDisplayFontFamily = FontFamily(
    Font(googleFont = playfairDisplayFont, fontProvider = googleFontProvider, weight = FontWeight.Normal),
    Font(googleFont = playfairDisplayFont, fontProvider = googleFontProvider, weight = FontWeight.Bold),
)

val LatoFontFamily = FontFamily(
    Font(googleFont = latoFont, fontProvider = googleFontProvider, weight = FontWeight.Normal),
    Font(googleFont = latoFont, fontProvider = googleFontProvider, weight = FontWeight.Medium),
    Font(googleFont = latoFont, fontProvider = googleFontProvider, weight = FontWeight.Bold),
)

internal fun buildTypography(
    serifFamily: FontFamily = PlayfairDisplayFontFamily,
    sansFamily: FontFamily = LatoFontFamily,
) = Typography(
    displayLarge = TextStyle(
        fontFamily = serifFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = serifFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = serifFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 30.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = serifFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = sansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = sansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = sansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = sansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
    ),
)

val Typography = buildTypography()
