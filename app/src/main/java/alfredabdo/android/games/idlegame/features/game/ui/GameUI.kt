package alfredabdo.android.games.idlegame.features.game.ui

import alfredabdo.android.games.idlegame.R
import alfredabdo.android.games.idlegame.data.GameAchievement
import alfredabdo.android.games.idlegame.data.GameItem
import alfredabdo.android.games.idlegame.features.game.data.HomeGameItemState
import alfredabdo.android.games.idlegame.features.game.data.HomeGameState
import alfredabdo.android.games.idlegame.ui.icons.SaveIcon
import alfredabdo.android.games.idlegame.ui.theme.AppTheme
import alfredabdo.android.games.idlegame.util.game.GameFormatter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.isActive
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Composable
fun GameUI(
    state: HomeGameState,
    onPurchase: (item: GameItem) -> Unit,
    onUpgrade: (item: GameItem) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    isAnimatingSave: Boolean = false,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val gameFormatter = remember { GameFormatter }

    val coins = remember(state.coins) { state.coins }
    val itemStates = state.states.toList()
    val activeAchievement = state.activeAchievement
    val lastVisibleItemIndex = itemStates.indexOfFirst { !it.second.unlocked }

    val saveRotationAnimatable = remember { Animatable(0f) }


    Scaffold(
        modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSave,
                Modifier.rotate(saveRotationAnimatable.value),
            ) {
                Icon(SaveIcon, stringResource(R.string.save))
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
//        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        LazyColumn(
            Modifier
                .consumeWindowInsets(padding)
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                GameActiveAchievementContent(
                    activeAchievement,
                    Modifier.fillMaxWidth()
                )
            }
            stickyHeader {
                GameCoinsContent(
                    coins,
                    gameFormatter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
                )
            }
            itemsIndexed(itemStates) { index, (item, state) ->
                AnimatedVisibility(
                    lastVisibleItemIndex == -1 || index <= lastVisibleItemIndex,
                    enter = fadeIn() + expandIn(
                        expandFrom = Alignment.Center,
                        initialSize = { IntSize(it.width, 0) },
                    ),
                ) {
                    GameItemContent(
                        item,
                        state,
                        gameFormatter,
                        onPurchase = onPurchase,
                        onUpgrade = onUpgrade,
                        Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }


    LaunchedEffect(isAnimatingSave) {
        try {
            while (isActive && isAnimatingSave) {
                saveRotationAnimatable.animateTo(360f, tween(500))
                saveRotationAnimatable.snapTo(0f)
            }
        } finally {
            saveRotationAnimatable.snapTo(0f)
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun GameUI_Preview() {
    AppTheme {
        val state = remember {
            HomeGameState(
                1000.0,
                mutableStateMapOf(
                    // test below threshold
                    GameItem(
                        "1",
                        "Pocket change",
                        "Rely on your parents, ...",
                        2.seconds,
                        10.0,
                        GameItem.UpgradeMultipliers(1.5, 1.2, 1.3),
                        0.0,
                        20.0,
                    ) to HomeGameItemState(
                        "1",
                        100,
                        true,
                        100.milliseconds,
                        10.0,
                        20.0,
                    ),
                    // test unlocked
                    GameItem(
                        "2",
                        "Work as an employee",
                        "Become a developer in a company.",
                        5.seconds,
                        90.0,
                        GameItem.UpgradeMultipliers(1.5, 1.2, 1.3),
                        100.0,
                        200.0,
                    ) to HomeGameItemState(
                        "2",
                        1,
                        true,
                        5.seconds,
                        90.0,
                        200.0,
                    ),
                    // test locked
                    GameItem(
                        "3",
                        "Freelancer",
                        "Work as a developer freelancer, with no one above you :).",
                        30.seconds,
                        500.0,
                        GameItem.UpgradeMultipliers(1.5, 1.2, 1.3),
                        500.0,
                        700.0,
                    ) to HomeGameItemState(
                        "3",
                        1,
                        false,
                        30.seconds,
                        500.0,
                        700.0,
                    ),
                ),
                GameAchievement(
                    "1",
                    "Purchase your first source of income!",
                    "3, 2, 1, Go!",
                ) { _, itemStates ->
                    itemStates.entries.firstOrNull()
                        ?.value
                        ?.unlocked ?: false
                },
            )
        }

        GameUI(
            state,
            onPurchase = {},
            onUpgrade = {},
            onSave = {},
            Modifier.fillMaxSize(),
        )
    }
}