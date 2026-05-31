package com.sakura.flowdrive.ui.theme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat
import com.sakura.flowdrive.core.util.AppSettings

val LightColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF22005D),
    secondary = PurpleGrey40,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1D192B),
    tertiary = Pink40,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF492D35),
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    surfaceTint = Purple40,
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFC9C5D0)
)

val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color.White,
    secondary = PurpleGrey80,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = Color.White,
    tertiary = Pink80,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFF633B48),
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E8),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E8),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    surfaceTint = Purple80,
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F)
)

val AmoledColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color.White,
    secondary = PurpleGrey80,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF1A1A1A),
    onSecondaryContainer = Color(0xFFCAC4D0),
    tertiary = Pink80,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFF633B48),
    background = Color.Black,
    onBackground = Color(0xFFE6E1E8),
    surface = Color.Black,
    onSurface = Color(0xFFE6E1E8),
    surfaceVariant = Color(0xFF1A1A1A),
    onSurfaceVariant = Color(0xFFCAC4D0),
    surfaceTint = Purple80,
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF2A2A2A)
)

@Composable
fun FlowDriveTheme(
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkModeIndex = AppSettings.darkModeIndex
    val isAmoled = AppSettings.isAmoled

    val isDark = when (darkModeIndex) {
        1 -> false
        2 -> true
        else -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDark) {
                val base = dynamicDarkColorScheme(context)
                if (isAmoled) base.copy(
                    background = Color.Black,
                    surface = Color.Black,
                    surfaceVariant = Color(0xFF1A1A1A),
                    outlineVariant = Color(0xFF2A2A2A)
                ) else base
            } else dynamicLightColorScheme(context)
        }
        isDark && isAmoled -> AmoledColorScheme
        isDark -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
        }
    }

    val fontFamily = if (AppSettings.useSystemFont) {
        FontFamily.Default
    } else {
        HarmonyOsSans
    }
    val typography = buildTypography(fontFamily)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
