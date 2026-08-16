package alfredabdo.android.games.idlegame.features.home

import alfredabdo.android.games.idlegame.R
import alfredabdo.android.games.idlegame.features.home.ui.HomeUI
import alfredabdo.android.games.idlegame.ui.dialog.MainAlertDialog
import alfredabdo.android.games.idlegame.ui.dialog.MainAlertDialogDefaults
import alfredabdo.android.games.idlegame.util.viewmodel.appViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
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
    onQuitApp: () -> Unit,
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
        MainAlertDialog(
            onDismissRequest = viewModel::hideDeleteConfirmation,
            title = {
                MainAlertDialogDefaults.TitleText(stringResource(R.string.warning_))
            },
            text = {
                MainAlertDialogDefaults.BodyText(stringResource(R.string.delete_confirmation__message))
            },
            confirmButton = {
                MainAlertDialogDefaults.DismissButton({
                    viewModel.confirmDelete()
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                MainAlertDialogDefaults.DismissButton(viewModel::hideDeleteConfirmation) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }
    if (isDeleting) {
        MainAlertDialog(
            onDismissRequest = {
                viewModel.hideIsDeletingAlert()
                onQuitApp()
            },
            title = {
                MainAlertDialogDefaults.TitleText(stringResource(R.string.deleting_))
            },
            text = {
                MainAlertDialogDefaults.BodyText(stringResource(R.string.deleting__message))
            },
            confirmButton = {
                MainAlertDialogDefaults.DismissButton({
                    viewModel.hideIsDeletingAlert()
                    onQuitApp()
                }) {
                    Text(stringResource(R.string.ok))
                }
            },
        )
    }
}