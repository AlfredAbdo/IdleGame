package alfredabdo.android.games.idlegame.ui.theme

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AppColors.Purple80,
    secondary = AppColors.PurpleGrey80,
    tertiary = AppColors.Pink80,
)

private val LightColorScheme = lightColorScheme(
    primary = AppColors.Purple40,
    secondary = AppColors.PurpleGrey40,
    tertiary = AppColors.Pink40,
)

@Composable
fun AppTheme(
    darkTheme: Boolean? = null,
    content: @Composable () -> Unit
) {
    val themeViewModel = themeViewModel
    val lightDarkMode by themeViewModel.lightDarkMode.collectAsState()
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val isDarkThemeResolved = remember(darkTheme, lightDarkMode) {
        when (darkTheme) {
            true -> true
            false -> false
            else -> {
                when (lightDarkMode) {
                    LightDarkMode.SYSTEM -> isSystemInDarkTheme
                    LightDarkMode.DARK -> true
                    LightDarkMode.LIGHT -> false
                }
            }
        }
    }

    val colorScheme = if (isDarkThemeResolved) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    val activity = LocalActivity.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = activity!!.window
            //window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkThemeResolved
        }
    }

    CompositionLocalProvider(
        LocalLightDarkMode provides lightDarkMode,
        LocalIsDarkMode provides isDarkThemeResolved,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = Shapes,
            typography = Typography,
            content = content
        )
    }
}


val LocalLightDarkMode = compositionLocalOf { LightDarkMode.SYSTEM }
private val LocalIsDarkMode = compositionLocalOf { false }

object AppTheme {

    val lightDarkMode
        @Composable
        @ReadOnlyComposable
        get() = LocalLightDarkMode.current

    val isDarkMode
        @Composable
        @ReadOnlyComposable
        get() = LocalIsDarkMode.current
}