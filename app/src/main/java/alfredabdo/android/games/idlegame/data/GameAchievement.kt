package alfredabdo.android.games.idlegame.data

import androidx.compose.runtime.Immutable

@Immutable
class GameAchievement(
    val id: String,
    val text: String,
    val popup: String,
    val condition: (
        coins: Double,
        itemStates: Map<GameItem, GameItemState>,
    ) -> Boolean,
)