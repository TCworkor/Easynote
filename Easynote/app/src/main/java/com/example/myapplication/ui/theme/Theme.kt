package com.example.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.part.ThemePreference

//000000000000000000000000000000000000000000000000000000000000000000000000
private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    error = md_theme_light_error,
    errorContainer = md_theme_light_errorContainer,
    onError = md_theme_light_onError,
    onErrorContainer = md_theme_light_onErrorContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    outline = md_theme_light_outline,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    inversePrimary = md_theme_light_inversePrimary,
    surfaceTint = md_theme_light_surfaceTint,
    outlineVariant = md_theme_light_outlineVariant,
    scrim = md_theme_light_scrim,
)


private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim,
)

//1111111111111111111111111111111111111111111111111

private val LightColors1 = lightColorScheme(
    primary = md_theme_light_primary1,
    onPrimary = md_theme_light_onPrimary1,
    primaryContainer = md_theme_light_primaryContainer1,
    onPrimaryContainer = md_theme_light_onPrimaryContainer1,
    secondary = md_theme_light_secondary1,
    onSecondary = md_theme_light_onSecondary1,
    secondaryContainer = md_theme_light_secondaryContainer1,
    onSecondaryContainer = md_theme_light_onSecondaryContainer1,
    tertiary = md_theme_light_tertiary1,
    onTertiary = md_theme_light_onTertiary1,
    tertiaryContainer = md_theme_light_tertiaryContainer1,
    onTertiaryContainer = md_theme_light_onTertiaryContainer1,
    error = md_theme_light_error1,
    onError = md_theme_light_onError1,
    errorContainer = md_theme_light_errorContainer1,
    onErrorContainer = md_theme_light_onErrorContainer1,
    outline = md_theme_light_outline1,
    background = md_theme_light_background1,
    onBackground = md_theme_light_onBackground1,
    surface = md_theme_light_surface1,
    onSurface = md_theme_light_onSurface1,
    surfaceVariant = md_theme_light_surfaceVariant1,
    onSurfaceVariant = md_theme_light_onSurfaceVariant1,
    inverseSurface = md_theme_light_inverseSurface1,
    inverseOnSurface = md_theme_light_inverseOnSurface1,
    inversePrimary = md_theme_light_inversePrimary1,
    surfaceTint = md_theme_light_surfaceTint1,
    outlineVariant = md_theme_light_outlineVariant1,
    scrim = md_theme_light_scrim1,
)

private val DarkColors1 = darkColorScheme(
    primary = md_theme_dark_primary1,
    onPrimary = md_theme_dark_onPrimary1,
    primaryContainer = md_theme_dark_primaryContainer1,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer1,
    secondary = md_theme_dark_secondary1,
    onSecondary = md_theme_dark_onSecondary1,
    secondaryContainer = md_theme_dark_secondaryContainer1,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer1,
    tertiary = md_theme_dark_tertiary1,
    onTertiary = md_theme_dark_onTertiary1,
    tertiaryContainer = md_theme_dark_tertiaryContainer1,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer1,
    error = md_theme_dark_error1,
    onError = md_theme_dark_onError1,
    errorContainer = md_theme_dark_errorContainer1,
    onErrorContainer = md_theme_dark_onErrorContainer1,
    outline = md_theme_dark_outline1,
    background = md_theme_dark_background1,
    onBackground = md_theme_dark_onBackground1,
    surface = md_theme_dark_surface1,
    onSurface = md_theme_dark_onSurface1,
    surfaceVariant = md_theme_dark_surfaceVariant1,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant1,
    inverseSurface = md_theme_dark_inverseSurface1,
    inverseOnSurface = md_theme_dark_inverseOnSurface1,
    inversePrimary = md_theme_dark_inversePrimary1,
    surfaceTint = md_theme_dark_surfaceTint1,
    outlineVariant = md_theme_dark_outlineVariant1,
    scrim = md_theme_dark_scrim1,
)


//2222222222222222222222222222222222222222222222222222222222222
private val LightColors2 = lightColorScheme(
    primary = md_theme_light_primary2,
    onPrimary = md_theme_light_onPrimary2,
    primaryContainer = md_theme_light_primaryContainer2,
    onPrimaryContainer = md_theme_light_onPrimaryContainer2,
    secondary = md_theme_light_secondary2,
    onSecondary = md_theme_light_onSecondary2,
    secondaryContainer = md_theme_light_secondaryContainer2,
    onSecondaryContainer = md_theme_light_onSecondaryContainer2,
    tertiary = md_theme_light_tertiary2,
    onTertiary = md_theme_light_onTertiary2,
    tertiaryContainer = md_theme_light_tertiaryContainer2,
    onTertiaryContainer = md_theme_light_onTertiaryContainer2,
    error = md_theme_light_error2,
    errorContainer = md_theme_light_errorContainer2,
    onError = md_theme_light_onError2,
    onErrorContainer = md_theme_light_onErrorContainer2,
    background = md_theme_light_background2,
    onBackground = md_theme_light_onBackground2,
    surface = md_theme_light_surface2,
    onSurface = md_theme_light_onSurface2,
    surfaceVariant = md_theme_light_surfaceVariant2,
    onSurfaceVariant = md_theme_light_onSurfaceVariant2,
    outline = md_theme_light_outline2,
    inverseOnSurface = md_theme_light_inverseOnSurface2,
    inverseSurface = md_theme_light_inverseSurface2,
    inversePrimary = md_theme_light_inversePrimary2,
    surfaceTint = md_theme_light_surfaceTint2,
    outlineVariant = md_theme_light_outlineVariant2,
    scrim = md_theme_light_scrim2,
)

private val DarkColors2 = darkColorScheme(
    primary = md_theme_dark_primary2,
    onPrimary = md_theme_dark_onPrimary2,
    primaryContainer = md_theme_dark_primaryContainer2,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer2,
    secondary = md_theme_dark_secondary2,
    onSecondary = md_theme_dark_onSecondary2,
    secondaryContainer = md_theme_dark_secondaryContainer2,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer2,
    tertiary = md_theme_dark_tertiary2,
    onTertiary = md_theme_dark_onTertiary2,
    tertiaryContainer = md_theme_dark_tertiaryContainer2,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer2,
    error = md_theme_dark_error2,
    errorContainer = md_theme_dark_errorContainer2,
    onError = md_theme_dark_onError2,
    onErrorContainer = md_theme_dark_onErrorContainer2,
    background = md_theme_dark_background2,
    onBackground = md_theme_dark_onBackground2,
    surface = md_theme_dark_surface2,
    onSurface = md_theme_dark_onSurface2,
    surfaceVariant = md_theme_dark_surfaceVariant2,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant2,
    outline = md_theme_dark_outline2,
    inverseOnSurface = md_theme_dark_inverseOnSurface2,
    inverseSurface = md_theme_dark_inverseSurface2,
    inversePrimary = md_theme_dark_inversePrimary2,
    surfaceTint = md_theme_dark_surfaceTint2,
    outlineVariant = md_theme_dark_outlineVariant2,
    scrim = md_theme_dark_scrim2,
)

//3333333333333333333333333333333333333333333333333333333

private val LightColors3 = lightColorScheme(
    primary = md_theme_light_primary3,
    onPrimary = md_theme_light_onPrimary3,
    primaryContainer = md_theme_light_primaryContainer3,
    onPrimaryContainer = md_theme_light_onPrimaryContainer3,
    secondary = md_theme_light_secondary3,
    onSecondary = md_theme_light_onSecondary3,
    secondaryContainer = md_theme_light_secondaryContainer3,
    onSecondaryContainer = md_theme_light_onSecondaryContainer3,
    tertiary = md_theme_light_tertiary3,
    onTertiary = md_theme_light_onTertiary3,
    tertiaryContainer = md_theme_light_tertiaryContainer3,
    onTertiaryContainer = md_theme_light_onTertiaryContainer3,
    error = md_theme_light_error3,
    errorContainer = md_theme_light_errorContainer3,
    onError = md_theme_light_onError3,
    onErrorContainer = md_theme_light_onErrorContainer3,
    background = md_theme_light_background3,
    onBackground = md_theme_light_onBackground3,
    surface = md_theme_light_surface3,
    onSurface = md_theme_light_onSurface3,
    surfaceVariant = md_theme_light_surfaceVariant3,
    onSurfaceVariant = md_theme_light_onSurfaceVariant3,
    outline = md_theme_light_outline3,
    inverseOnSurface = md_theme_light_inverseOnSurface3,
    inverseSurface = md_theme_light_inverseSurface3,
    inversePrimary = md_theme_light_inversePrimary3,
    surfaceTint = md_theme_light_surfaceTint3,
    outlineVariant = md_theme_light_outlineVariant3,
    scrim = md_theme_light_scrim3,
)


private val DarkColors3 = darkColorScheme(
    primary = md_theme_dark_primary3,
    onPrimary = md_theme_dark_onPrimary3,
    primaryContainer = md_theme_dark_primaryContainer3,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer3,
    secondary = md_theme_dark_secondary3,
    onSecondary = md_theme_dark_onSecondary3,
    secondaryContainer = md_theme_dark_secondaryContainer3,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer3,
    tertiary = md_theme_dark_tertiary3,
    onTertiary = md_theme_dark_onTertiary3,
    tertiaryContainer = md_theme_dark_tertiaryContainer3,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer3,
    error = md_theme_dark_error3,
    errorContainer = md_theme_dark_errorContainer3,
    onError = md_theme_dark_onError3,
    onErrorContainer = md_theme_dark_onErrorContainer3,
    background = md_theme_dark_background3,
    onBackground = md_theme_dark_onBackground3,
    surface = md_theme_dark_surface3,
    onSurface = md_theme_dark_onSurface3,
    surfaceVariant = md_theme_dark_surfaceVariant3,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant3,
    outline = md_theme_dark_outline3,
    inverseOnSurface = md_theme_dark_inverseOnSurface3,
    inverseSurface = md_theme_dark_inverseSurface3,
    inversePrimary = md_theme_dark_inversePrimary3,
    surfaceTint = md_theme_dark_surfaceTint3,
    outlineVariant = md_theme_dark_outlineVariant3,
    scrim = md_theme_dark_scrim3,
)

//////cry

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val context = LocalContext.current
    val themeSetting = ThemePreference.getTheme(context)

    val colors = when (themeSetting) {
        1 -> if (!useDarkTheme) LightColors1 else DarkColors1
        2 -> if (!useDarkTheme) LightColors2 else DarkColors2
        3 -> if (!useDarkTheme) LightColors3 else DarkColors3
        4 -> if (!useDarkTheme) LightColors else DarkColors
        else -> if (!useDarkTheme) LightColors else DarkColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
