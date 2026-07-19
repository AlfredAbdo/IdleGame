package alfredabdo.android.games.idlegame.features.home.ui

import alfredabdo.android.games.idlegame.R
import alfredabdo.android.games.idlegame.ui.icons.DeleteIcon
import alfredabdo.android.games.idlegame.ui.icons.GameIcon
import alfredabdo.android.games.idlegame.ui.icons.SettingsIcon
import alfredabdo.android.games.idlegame.ui.preview.AppPreview
import alfredabdo.android.games.idlegame.ui.theme.AppTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun HomeUI(
    modifier: Modifier = Modifier,
    onGoToGame: () -> Unit,
    onGoToSettings: () -> Unit,
    onDelete: () -> Unit,
) {
    Column(
        modifier
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { onGoToGame() },
            Modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(GameIcon, stringResource(R.string.game))
                Text(stringResource(R.string.game))
            }
        }
        Spacer(Modifier.height(8.dp))
        FilledTonalButton(
            onClick = { onGoToSettings() },
            Modifier.fillMaxWidth(),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(SettingsIcon, stringResource(R.string.settings))
                Text(stringResource(R.string.settings))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onDelete,
            Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
            ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(DeleteIcon, stringResource(R.string.delete))
                Spacer(Modifier.width(4.dp))
                Text(stringResource(R.string.delete_your_save_))
            }
        }
    }
}


@AppPreview
@Composable
private fun HomeUI_Preview() {
    AppTheme {
        HomeUI(
            Modifier.fillMaxSize(),
            onGoToGame = {},
            onGoToSettings = {},
            onDelete = {},
        )
    }
}