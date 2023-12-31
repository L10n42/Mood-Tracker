package com.kappdev.moodtracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.kappdev.moodtracker.domain.util.Theme


private val DarkColorScheme = darkColorScheme(
    primary = AsparagusGreen,
    onPrimary = LinenWhite,

    secondary = SealBrown,
    onSecondary = LinenWhite,

    background = BlackOlive,
    onBackground = PaleTaupe,

    surface = SealBrown,
    onSurface = LinenWhite,

    error = MutedErrorRed,
    onError = LinenWhite
)

private val LightColorScheme = lightColorScheme(
    primary = Salmon,
    onPrimary = EclipseBlack,

    secondary = CottonSeed,
    onSecondary = EclipseBlack,

    background = Alabaster,
    onBackground = GrayOlive,

    surface = CottonSeed,
    onSurface = EclipseBlack,

    error = ErrorRed,
    onError = EclipseBlack
)

@Composable
fun MoodTrackerTheme(
    theme: Theme,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val darkTheme = when(theme) {
        Theme.LIGHT -> false
        Theme.DARK -> true
        Theme.SYSTEM_DEFAULT -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}