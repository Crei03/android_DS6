package com.proyect.ds6.ui.theme

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

// Esquema de colores para modo oscuro
private val DS6DarkColorScheme = darkColorScheme(
    primary = DS6PrimaryLight,
    onPrimary = DS6OnPrimary,
    primaryContainer = DS6Primary,
    onPrimaryContainer = DS6OnPrimary,
    background = DS6Background,
    onBackground = DS6OnBackground,
    surface = DS6Surface,
    onSurface = DS6OnSurface,
    error = DS6Error,
    onError = DS6OnError,
    outline = DS6Outline,
    surfaceVariant = DS6SurfaceVariant,
    onSurfaceVariant = DS6OnSurfaceVariant
)

// Esquema de colores para modo claro
private val DS6LightColorScheme = lightColorScheme(
    primary = DS6Primary,
    onPrimary = DS6OnPrimary,
    primaryContainer = DS6PrimaryLight,
    onPrimaryContainer = DS6OnBackground,
    background = DS6Background,
    onBackground = DS6OnBackground,
    surface = DS6Surface,
    onSurface = DS6OnSurface,
    error = DS6Error,
    onError = DS6OnError,
    outline = DS6Outline,
    surfaceVariant = DS6SurfaceVariant,
    onSurfaceVariant = DS6OnSurfaceVariant
)

@Composable
fun DS6Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color está disponible en Android 12+
    dynamicColor: Boolean = false, // Desactivado por defecto para mantener nuestra marca
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DS6DarkColorScheme
        else -> DS6LightColorScheme
    }
    
    // Aplicar el color de la barra de estado
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}