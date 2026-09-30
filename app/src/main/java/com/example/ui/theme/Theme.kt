package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BioBlueLight,
    onPrimary = Color.White,
    primaryContainer = BioBlueDark,
    onPrimaryContainer = Color.White,
    secondary = EmeraldBio,
    onSecondary = Color.White,
    secondaryContainer = EmeraldDark,
    onSecondaryContainer = Color.White,
    tertiary = AmberBond,
    onTertiary = Color.Black,
    background = DarkNavyBg,
    surface = DarkNavySurface,
    surfaceVariant = DarkNavyCard,
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF1F5F9),
    outline = DarkNavyBorder,
    error = CrimsonAlert
)

private val LightColorScheme = lightColorScheme(
    primary = BioBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = BioBlueDark,
    secondary = EmeraldBio,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD1FAE5),
    onSecondaryContainer = EmeraldDark,
    tertiary = AmberBond,
    onTertiary = Color.Black,
    background = LightBioBg,
    surface = LightBioSurface,
    surfaceVariant = Color(0xFFF1F5F9),
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    outline = LightBioBorder,
    error = CrimsonAlert
)

@Composable
fun ProteinBuilderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent biology theme branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
