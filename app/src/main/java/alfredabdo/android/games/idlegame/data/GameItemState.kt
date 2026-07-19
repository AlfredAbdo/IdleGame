package alfredabdo.android.games.idlegame.data

import kotlin.time.Duration

interface GameItemState {
    val id: String
    val level: Int
    val unlocked: Boolean
    val fillRate: Duration
    val gain: Double
    val upgradeCost: Double
    val progress: Double
}