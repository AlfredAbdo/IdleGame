package alfredabdo.android.games.idlegame.features.settings

import alfredabdo.android.games.idlegame.features.settings.ui.SettingsUI
import alfredabdo.android.games.idlegame.ui.theme.AppTheme
import alfredabdo.android.games.idlegame.ui.theme.themeViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsPage() {
    val themeViewModel = themeViewModel
    val lightDarkMode = AppTheme.lightDarkMode

    SettingsUI(
        lightDarkMode = lightDarkMode,
        onChangeLightDarkMode = themeViewModel::changeLightDarkMode,
        Modifier.fillMaxSize(),
    )
}