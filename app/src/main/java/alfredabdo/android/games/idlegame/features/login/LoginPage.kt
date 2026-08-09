package alfredabdo.android.games.idlegame.features.login

import alfredabdo.android.games.idlegame.features.login.ui.LoginUI
import alfredabdo.android.games.idlegame.ui.state.LoadingIndicator
import alfredabdo.android.games.idlegame.ui.state.UIState
import alfredabdo.android.games.idlegame.util.viewmodel.appViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginPage(
    onGoToHome: () -> Unit,
    viewModel: LoginViewModel = appViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var errorMessageToShow: String? by remember { mutableStateOf(null) }

    suspend fun handleEvent(event: LoginViewModel.Event) {
        when (event) {
            is LoginViewModel.Event.Error -> errorMessageToShow = event.exception.message
            LoginViewModel.Event.RedirectToHome -> onGoToHome()
        }
    }

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect(::handleEvent)
    }

    LaunchedEffect(viewModel.tempEvents) {
        viewModel.tempEvents.collect(::handleEvent)
    }


    when (val state = state) {
        is LoginViewModel.State.Fetching -> {
            LoadingIndicator(UIState.Loading(), Modifier.fillMaxSize())
        }

        is LoginViewModel.State.Fetched -> {
            LoginUI(
                state.loggedInUsername.orEmpty(),
                state.hasLocalSave,
                onLogin = { username, carryOverSave ->
                    viewModel.runAction(LoginViewModel.Action.Login(username, carryOverSave))
                },
                onUseLocalSave = {
                    viewModel.runAction(LoginViewModel.Action.UseLocalSave)
                },
                Modifier.fillMaxSize(),
            )

            if (state.showLoading) {
                BlockingLoadingIndicator(onDismissRequest = {})
            }
        }
    }


    LaunchedEffect(Unit) {
        viewModel.runAction(LoginViewModel.Action.FetchSaveInfo)
    }

    errorMessageToShow?.let { message ->
        ErrorDialog(
            onDismissRequest = { errorMessageToShow = null },
            message,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BlockingLoadingIndicator(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicAlertDialog(
        onDismissRequest,
        modifier,
    ) {
        Surface(
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Box(Modifier.padding(24.dp)) {
                CircularProgressIndicator(
                    Modifier.align(Alignment.Center),
                )
            }
        }
    }
}

@Composable
private fun ErrorDialog(
    onDismissRequest: () -> Unit,
    message: String,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest,
        confirmButton = { TextButton(onClick = onDismissRequest) { Text("OK") } },
        modifier,
        text = { Text(message) }
    )
}