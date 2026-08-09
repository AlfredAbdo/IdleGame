package alfredabdo.android.games.idlegame.features.login.ui

import alfredabdo.android.games.idlegame.R
import alfredabdo.android.games.idlegame.ui.preview.AppPreview
import alfredabdo.android.games.idlegame.ui.theme.AppTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun LoginUI(
    username: String,
    hasLocalSave: Boolean,
    onLogin: (username: String, carryOverSave: Boolean) -> Unit,
    onUseLocalSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val usernameTextField = rememberTextFieldState(username)
        var carryOverSave by rememberSaveable { mutableStateOf(false) }


        TextField(
            usernameTextField,
            Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.username)) },
        )

        if (hasLocalSave) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { carryOverSave = !carryOverSave },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    carryOverSave,
                    onCheckedChange = null,
                )
                Spacer(Modifier.width(8.dp))
                Text("Carry Over Save")
            }
        }

        Button(
            onClick = {
                onLogin(
                    usernameTextField.text.toString(),
                    carryOverSave,
                )
            },
            Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.login))
        }

        if (hasLocalSave) {
            HorizontalDivider()

            ElevatedButton(
                onClick = onUseLocalSave,
                Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.use_local_save))
            }
        }
    }
}


@AppPreview
@Composable
private fun LoginUI_Test() {
    AppTheme {
        LoginUI(
            username = "",
            hasLocalSave = true,
            onLogin = { _, _ -> },
            onUseLocalSave = {},
            Modifier.fillMaxSize(),
        )
    }
}