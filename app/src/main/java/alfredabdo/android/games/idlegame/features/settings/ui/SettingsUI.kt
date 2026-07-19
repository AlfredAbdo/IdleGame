package alfredabdo.android.games.idlegame.features.settings.ui

import alfredabdo.android.games.idlegame.ui.preview.AppPreview
import alfredabdo.android.games.idlegame.ui.theme.AppTheme
import alfredabdo.android.games.idlegame.ui.theme.LightDarkMode
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsUI(
    lightDarkMode: LightDarkMode,
    onChangeLightDarkMode: (mode: LightDarkMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            "Dark mode:",
            Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
        )
        Spacer(Modifier.height(8.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .selectableGroup(),
        ) {
            LightDarkModeOption(
                "System",
                isSelected = lightDarkMode == LightDarkMode.SYSTEM,
                onSelected = { onChangeLightDarkMode(LightDarkMode.SYSTEM) },
                modifier = Modifier
                    .fillMaxWidth(),
            )
            LightDarkModeOption(
                "Light",
                isSelected = lightDarkMode == LightDarkMode.LIGHT,
                onSelected = { onChangeLightDarkMode(LightDarkMode.LIGHT) },
                modifier = Modifier
                    .fillMaxWidth(),
            )
            LightDarkModeOption(
                "Dark",
                isSelected = lightDarkMode == LightDarkMode.DARK,
                onSelected = { onChangeLightDarkMode(LightDarkMode.DARK) },
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}


@Composable
private fun LightDarkModeOption(
    text: String,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .selectable(
                isSelected,
                onClick = { onSelected() },
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = isSelected, onClick = { onSelected() })
        Text(text = text)
    }
}


@AppPreview
@Composable
private fun SettingsUI_Preview() {
    AppTheme {
        SettingsUI(
            LightDarkMode.SYSTEM,
            onChangeLightDarkMode = {},
        )
    }
}