package alfredabdo.android.games.idlegame.features.game.ui

import alfredabdo.android.games.idlegame.data.GameItem
import alfredabdo.android.games.idlegame.features.game.data.HomeGameItemState
import alfredabdo.android.games.idlegame.ui.animation.gameInfiniteAnimationThreshold
import alfredabdo.android.games.idlegame.ui.theme.AppTheme
import alfredabdo.android.games.idlegame.ui.theme.progressBrush
import alfredabdo.android.games.idlegame.ui.theme.progressInfiniteBrush
import alfredabdo.android.games.idlegame.util.game.GameFormatter
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Composable
fun GameItemContent(
    item: GameItem,
    state: HomeGameItemState,
    gameFormatter: GameFormatter,
    onPurchase: (item: GameItem) -> Unit,
    onUpgrade: (item: GameItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val progress by remember(item.id, state.progress) { mutableDoubleStateOf(state.progress) }
    val isLocked = remember(item.id, state.unlocked) { !state.unlocked }
    val gainAmount = remember(state.gain) { gameFormatter.formatAmount(state.gain) }
    val unlockAmount = remember(item.unlockAmount) { item.unlockAmount?.let { gameFormatter.formatAmount(it) } }
    val upgradeAmount = remember(state.upgradeCost) { gameFormatter.formatAmount(state.upgradeCost) }
    val fillRate = remember(state.fillRate) { state.fillRate }

    Row(
        modifier = modifier
            .gameItemBackground(
                progress = progress,
                shouldShowInfiniteTransition = { fillRate <= gameInfiniteAnimationThreshold },
            )
            .padding(16.dp)
            .height(IntrinsicSize.Min)
            .heightIn(min = 128.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Normal)) {
                        append("Gain: ")
                    }
                    append(gainAmount)
                    append(" coins each ")
                    append(gameFormatter.formatDuration(fillRate))
                },
                style = MaterialTheme.typography.labelLarge,
            )
        }

        Spacer(modifier = Modifier.width(2.dp))

        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically),
        ) {
            //fixme fully qualified name due to bug in Kotlin
            androidx.compose.animation.AnimatedVisibility(
                visible = isLocked,
                label = "GameCellPurchaseVisibility",
                enter = fadeIn(tween(500)) + slideInVertically(
                    tween(1_000), initialOffsetY = { -it }
                ),
                exit = fadeOut(tween(500)) + slideOutVertically(
                    tween(1_000), targetOffsetY = { it }
                ),
            ) {
                Button(onClick = { onPurchase(item) }) {
                    Text(
                        text = "Purchase for\n$unlockAmount",
                        textAlign = TextAlign.Center,
                    )
                }
            }
            androidx.compose.animation.AnimatedVisibility(
                visible = !isLocked,
                label = "GameCellUpgradeVisibility",
                enter = fadeIn(tween(500)) + slideInVertically(
                    tween(1_000), initialOffsetY = { -it }
                ),
                exit = fadeOut(tween(500)) + slideOutVertically(
                    tween(1_000), targetOffsetY = { it }
                ),
            ) {
                Button(onClick = { onUpgrade(item) }) {
                    Text(
                        text = "Upgrade for\n$upgradeAmount",
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun Modifier.gameItemBackground(
    progress: Double,
    shouldShowInfiniteTransition: () -> Boolean,
): Modifier {
    val density = LocalDensity.current
    val brush = progressBrush

    return border(1.dp, brush, RoundedCornerShape(16.dp))
        .run {
            if (shouldShowInfiniteTransition()) {
                val transition = rememberInfiniteTransition(
                    "GameItemBackgroundInfiniteTransition",
                )
                val animation = transition.animateFloat(
                    initialValue = 0f,
                    targetValue = 3_000f,
                    animationSpec = infiniteRepeatable(
                        tween(1_000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart,
                    ),
                    label = "GameItemBackgroundInfiniteAnimation",
                )

                background(
                    progressInfiniteBrush(Offset(x = animation.value, y = animation.value)),
                    RoundedCornerShape(16.dp)
                )
            } else {
                drawBehind {
                    clipRect(
                        right = (size.width * progress).toFloat(),
                    ) {
                        drawRoundRect(
                            brush,
                            cornerRadius = with(density) { CornerRadius(16.dp.toPx()) },
                        )
                    }
                }
            }
        }
}


@Preview(showBackground = true)
@Composable
private fun GameItemContent_Preview() {
    AppTheme {
        GameItemContent(
            GameItem(
                "1",
                "Pocket change",
                "Rely on your parents, ...",
                2.seconds,
                10.0,
                GameItem.UpgradeMultipliers(1.5, 1.5, 1.3),
                null,
                20.0,
            ),
            HomeGameItemState(
                "1",
                1,
                true,
                2.seconds,
                10.0,
                20.0,
            ),
            GameFormatter,
            onPurchase = {},
            onUpgrade = {},
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GameItemContent_PreviewBelowThreshold() {
    AppTheme {
        GameItemContent(
            GameItem(
                "1",
                "Pocket change",
                "Rely on your parents, ...",
                2.seconds,
                10.0,
                GameItem.UpgradeMultipliers(1.5, 1.5, 1.3),
                null,
                20.0,
            ),
            HomeGameItemState(
                "1",
                1,
                true,
                100.milliseconds,
                10.0,
                20.0,
            ),
            GameFormatter,
            onPurchase = {},
            onUpgrade = {},
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )
    }
}