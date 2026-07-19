package alfredabdo.android.games.idlegame.features.game.ui

import alfredabdo.android.games.idlegame.ui.theme.AppTheme
import alfredabdo.android.games.idlegame.util.game.GameFormatter
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GameCoinsContent(
    coins: Double,
    gameFormatter: GameFormatter,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "${gameFormatter.formatCoins(coins)} Coins",
        style = MaterialTheme.typography.displaySmall,
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}


@Preview(showBackground = true)
@Composable
private fun GameCoinsContent_Preview() {
    AppTheme {
        GameCoinsContent(
            1000.0,
            GameFormatter,
            Modifier.fillMaxWidth(),
        )
    }
}