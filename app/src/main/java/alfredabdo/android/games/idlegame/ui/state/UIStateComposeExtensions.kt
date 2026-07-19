package alfredabdo.android.games.idlegame.ui.state

import alfredabdo.android.games.idlegame.R
import alfredabdo.android.games.idlegame.data.exception.AppException
import alfredabdo.android.games.idlegame.ui.action.MainButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun <T> LoadingPage(
    result: UIState<T>?,
    onNull: @Composable () -> Unit = {},
    loadingContent: @Composable UIState.Loading.() -> Unit = {
        LoadingIndicator(this, Modifier.fillMaxSize())
    },
    errorContent: @Composable UIState.Error.(exception: AppException) -> Unit = {
        LoadingError(this, Modifier.fillMaxSize())
    },
    successContent: @Composable UIState<T>.(data: T) -> Unit,
) {
    when (result) {
        null -> onNull()
        is UIState.Loading -> result.loadingContent()
        is UIState.Error -> result.errorContent(result.exception)
        is UIState.Success -> result.successContent(result.data)
    }
}

@Composable
fun <T> LoadingBox(
    result: UIState<T>?,
    modifier: Modifier = Modifier,
    onNull: @Composable () -> Unit = {},
    loadingContent: @Composable UIState.Loading.() -> Unit = {
        LoadingIndicator(this, Modifier.fillMaxSize())
    },
    errorContent: @Composable UIState.Error.(exception: AppException) -> Unit = {
        LoadingError(this, Modifier.fillMaxSize())
    },
    successContent: @Composable UIState<T>.(data: T) -> Unit,
) {
    Box(modifier) {
        LoadingPage(
            result,
            onNull,
            loadingContent,
            errorContent,
            successContent,
        )
    }
}

@Composable
fun <T> LoadingHandler(
    result: UIState<T>?,
    onNull: suspend () -> Unit = {},
    onLoading: suspend UIState.Loading.() -> Unit = {},
    onError: suspend UIState.Error.(exception: AppException) -> Unit = {},
    onSuccess: suspend UIState.Success<T>.(data: T) -> Unit,
) {
    LaunchedEffect(result) {
        when (result) {
            null -> onNull()
            is UIState.Loading -> result.onLoading()
            is UIState.Error -> result.onError(result.exception)
            is UIState.Success -> result.onSuccess(result.data)
        }
    }
}

@Composable
fun LoadingIndicator(
    state: UIState.Loading,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        CircularProgressIndicator(
            Modifier.align(Alignment.Center),
        )
    }
}

@Composable
fun LoadingError(
    state: UIState.Error,
    modifier: Modifier = Modifier,
) {
    LoadingError(
        state.exception.localizedMessage.orEmpty(),
        onRetryAction = null,
        modifier = modifier,
    )
}

@Composable
fun LoadingError(
    message: String,
    onRetryAction: (() -> Unit)?,
    modifier: Modifier = Modifier,
    retryLabel: String = stringResource(R.string.retry),
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            message,
            textAlign = TextAlign.Center,
        )
        onRetryAction?.let { onRetryAction ->
            Spacer(Modifier.height(8.dp))
            MainButton(onClick = onRetryAction) {
                Text(retryLabel)
            }
        }
    }
}