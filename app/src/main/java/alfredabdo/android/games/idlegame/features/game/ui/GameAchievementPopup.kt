package alfredabdo.android.games.idlegame.features.game.ui

import alfredabdo.android.games.idlegame.R
import alfredabdo.android.games.idlegame.data.GameAchievement
import alfredabdo.android.games.idlegame.ui.theme.AppTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun GameAchievementAnimatedPopup(
    visible: Boolean,
    achievement: GameAchievement?,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible,
        modifier,
        enter = fadeIn(tween(500, easing = LinearEasing)) +
                expandIn(initialSize = { IntSize(it.width, 0) }),
        exit = shrinkOut(targetSize = { IntSize(it.width, 0) }) +
                fadeOut(tween(500, easing = LinearEasing)),
    ) {
        achievement?.let {
            GameAchievementPopup(
                it,
                Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun GameAchievementPopup(
    achievement: GameAchievement,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
    ) {
        Column(
            Modifier
                .padding(12.dp)
                .fillMaxWidth(),
        ) {
            Text(
                stringResource(R.string.achievement_obtained),
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
            )
            Text(
                achievement.popup,
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun GameAchievementPopup_Preview() {
    AppTheme {
        GameAchievementPopup(
            GameAchievement(
                "1",
                "Purchase your first source of income!",
                "3, 2, 1, Go!",
            ) { _, itemStates ->
                itemStates.entries.firstOrNull()
                    ?.value
                    ?.unlocked ?: false
            },
            Modifier.fillMaxWidth(),
        )
    }
}