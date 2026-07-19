package alfredabdo.android.games.idlegame.data

class GameSavedState(
    val coins: Double,
    val states: Map<GameItem, GameItemSavedState>,
    val activeAchievement: GameAchievement?,
)