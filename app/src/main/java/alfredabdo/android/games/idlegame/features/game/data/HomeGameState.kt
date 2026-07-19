package alfredabdo.android.games.idlegame.features.game.data

import alfredabdo.android.games.idlegame.data.GameAchievement
import alfredabdo.android.games.idlegame.data.GameItem
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateMapOf

@Immutable
data class HomeGameState(
    val coins: Double = 0.0,
    val states: MutableMap<GameItem, HomeGameItemState> = mutableStateMapOf(),
    val activeAchievement: GameAchievement? = null,
)