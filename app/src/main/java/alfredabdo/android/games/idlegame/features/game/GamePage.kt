package alfredabdo.android.games.idlegame.features.game

import alfredabdo.android.games.idlegame.data.GameAchievement
import alfredabdo.android.games.idlegame.features.game.ui.GameAchievementAnimatedPopup
import alfredabdo.android.games.idlegame.features.game.ui.GameUI
import alfredabdo.android.games.idlegame.ui.state.LoadingHandler
import alfredabdo.android.games.idlegame.ui.state.UIState
import alfredabdo.android.games.idlegame.util.viewmodel.appViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


@Composable
fun GamePage(
    viewModel: GameViewModel = appViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val activeAchievement = state.activeAchievement

    // A saveKey is used in order to reset the save interval whenever the user triggers a manual save
    var saveKey by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var isAnimatingSave by remember { mutableStateOf(false) }

    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle(UIState.Success(null))
    val showAchievementState by viewModel.showAchievementState.collectAsStateWithLifecycle()
    var achievementShown: GameAchievement? by remember { mutableStateOf(null) }

    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(Unit) {
        viewModel.start(System.nanoTime())
        while (coroutineContext.isActive) {
            withFrameNanos {
                viewModel.update(it)
            }
        }
    }
    LaunchedEffect(saveKey) {
        while (coroutineContext.isActive) {
            delay(30.seconds)
            viewModel.save()
        }
    }
    LaunchedEffect(activeAchievement?.id) {
        if (activeAchievement != null) {
            while (isActive) {
                viewModel.checkActiveAchievement()
                delay(200.milliseconds)
            }
        }
    }


    Box(
        Modifier.fillMaxSize(),
    ) {
        GameUI(
            state,
            onPurchase = { item -> viewModel.unlock(item.id) },
            onUpgrade = { item -> viewModel.upgrade(item.id) },
            onSave = {
                viewModel.save()
                saveKey = System.currentTimeMillis()
            },
            Modifier.fillMaxSize(),
            isAnimatingSave = isAnimatingSave,
            snackbarHostState = snackbarHostState,
        )

        //reason for this is we need to persist the item when hiding
        if (showAchievementState != null) {
            achievementShown = showAchievementState
        }
        GameAchievementAnimatedPopup(
            showAchievementState != null,
            achievementShown,
            Modifier.fillMaxWidth(),
        )
    }


    LoadingHandler(
        loadingState,
        onLoading = {
            isAnimatingSave = true
        },
        onError = { exception ->
            isAnimatingSave = false
            snackbarHostState.showSnackbar(exception.message.orEmpty())
        },
        onSuccess = { message ->
            isAnimatingSave = false
            if (message != null) {
                snackbarHostState.showSnackbar(message)
            }
        },
    )
}