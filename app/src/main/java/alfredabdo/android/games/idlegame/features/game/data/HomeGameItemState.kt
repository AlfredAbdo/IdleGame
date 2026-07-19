package alfredabdo.android.games.idlegame.features.game.data

import alfredabdo.android.games.idlegame.data.GameItemState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.time.Duration

@Stable
class HomeGameItemState(
    override val id: String,
    level: Int,
    unlocked: Boolean,
    fillRate: Duration,
    gain: Double,
    upgradeCost: Double,
    progress: Double = 0.0,
): GameItemState {
    override var level by mutableIntStateOf(level)
    override var unlocked by mutableStateOf(unlocked)
    override var fillRate by mutableStateOf(fillRate)
    override var gain by mutableDoubleStateOf(gain)
    override var upgradeCost by mutableDoubleStateOf(upgradeCost)
    override var progress by mutableDoubleStateOf(progress)
}