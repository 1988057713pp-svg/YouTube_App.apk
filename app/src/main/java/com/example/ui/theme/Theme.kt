package com.example.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = YouTubeRed,
    onPrimary = Color.White,
    primaryContainer = YouTubeDarkRed,
    onPrimaryContainer = Color.White,
    secondary = YtBlue,
    onSecondary = Color.Black,
    background = YtDarkBackground,
    onBackground = YtDarkTextPrimary,
    surface = YtDarkBackground,
    onSurface = YtDarkTextPrimary,
    surfaceVariant = YtDarkSurfaceVariant,
    onSurfaceVariant = YtDarkTextSecondary,
    outline = YtDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = YouTubeRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE5E5),
    onPrimaryContainer = YouTubeDarkRed,
    secondary = YtBlue,
    onSecondary = Color.White,
    background = YtLightBackground,
    onBackground = YtLightTextPrimary,
    surface = YtLightBackground,
    onSurface = YtLightTextPrimary,
    surfaceVariant = YtLightSurfaceVariant,
    onSurfaceVariant = YtLightTextSecondary,
    outline = YtLightBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = true,
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
