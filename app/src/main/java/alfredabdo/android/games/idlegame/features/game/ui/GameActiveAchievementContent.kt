package alfredabdo.android.games.idlegame.features.game.ui

import alfredabdo.android.games.idlegame.R
import alfredabdo.android.games.idlegame.data.GameAchievement
import alfredabdo.android.games.idlegame.ui.theme.AppTheme
import alfredabdo.android.games.idlegame.ui.theme.progressBrush
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GameActiveAchievementContent(
    activeAchievement: GameAchievement?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .border(
                2.dp,
                progressBrush,
                RoundedCornerShape(16.dp)
            )
            .padding(8.dp),
    ) {
        Text(
            stringResource(R.string.next_achievement_),
            Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = FontFamily.Cursive,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            activeAchievement?.text.orEmpty(),
            fontWeight = FontWeight.Medium,
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun GameActiveAchievementContent_Preview() {
    AppTheme {
        GameActiveAchievementContent(
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