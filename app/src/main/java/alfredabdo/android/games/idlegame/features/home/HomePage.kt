package alfredabdo.android.games.idlegame.features.home

import alfredabdo.android.games.idlegame.R
import alfredabdo.android.games.idlegame.features.home.ui.HomeUI
import alfredabdo.android.games.idlegame.util.viewmodel.appViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomePage(
    viewModel: HomeViewModel = appViewModel(),
    onGoToGame: () -> Unit,
    onGoToSettings: () -> Unit,
) {
    val isConfirmingDelete by viewModel.isConfirmingDelete.collectAsStateWithLifecycle()
    val isDeleting by viewModel.isDeleting.collectAsStateWithLifecycle()


    HomeUI(
        Modifier.fillMaxSize(),
        onGoToGame = onGoToGame,
        onGoToSettings = onGoToSettings,
        onDelete = viewModel::showDeleteConfirmation,
    )


    if (isConfirmingDelete) {
        AlertDialog(
            onDismissRequest = viewModel::hideDeleteConfirmation,
            title = {
                Text(stringResource(R.string.warning_))
            },
            text = {
                Text(stringResource(R.string.delete_confirmation__message))
            },
            confirmButton = {
                TextButton({
                    viewModel.confirmDelete()
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(viewModel::hideDeleteConfirmation) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }
    if (isDeleting) {
        AlertDialog(
            onDismissRequest = viewModel::hideIsDeletingAlert,
            title = {
                Text(stringResource(R.string.deleting_))
            },
            text = {
                Text(stringResource(R.string.deleting__message))
            },
            confirmButton = {
                TextButton(viewModel::hideIsDeletingAlert) {
                    Text(stringResource(R.string.ok))
                }
            },
        )
    }
}